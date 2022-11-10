package pi.restaurantapp.model.fragments.management.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.model.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.ingredient.IngredientAmountChange
import pi.restaurantapp.objects.data.ingredient.IngredientDetails
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.order.OrderDetails
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.enums.IngredientModificationType
import pi.restaurantapp.objects.enums.OrderStatus
import pi.restaurantapp.utils.StringFormatUtils
import java.math.BigDecimal
import java.util.*

class PreviewOrderViewModel : AbstractPreviewItemViewModel() {
    override val databasePath = "orders"
    var delivererId = ""

    private val _orderStatus: MutableLiveData<Int> = MutableLiveData<Int>()
    val orderStatus: LiveData<Int> = _orderStatus

    private val _statusChange: MutableLiveData<Pair<String, Int>> = MutableLiveData<Pair<String, Int>>()
    val statusChange: LiveData<Pair<String, Int>> = _statusChange

    private val _possibleDeliverers: MutableLiveData<MutableList<UserBasic>> = MutableLiveData<MutableList<UserBasic>>()
    val possibleDeliverers: LiveData<MutableList<UserBasic>> = _possibleDeliverers

    private val _userName: MutableLiveData<String> = MutableLiveData()
    val userName: LiveData<String> = _userName

    private val _item: MutableLiveData<Order> = MutableLiveData()
    val item: LiveData<Order> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<OrderBasic>() ?: OrderBasic()
        val details = snapshotsPair.details?.toObject<OrderDetails>() ?: OrderDetails()
        _item.value = Order(itemId, basic, details)
    }

    fun updateOrderStatus(newStatus: Int) {
        val time = Date().time
        Firebase.firestore.runTransaction { transaction ->
            transaction.update(dbRefBasic.document(itemId), "orderStatus", newStatus)
            transaction.update(dbRefDetails.document(itemId), "statusChanges.$time", newStatus)
        }.addOnSuccessListener {
            _orderStatus.value = newStatus
            _statusChange.value = StringFormatUtils.formatDateTime(Date(time)) to newStatus

            if (newStatus == OrderStatus.ACCEPTED.ordinal) {
                getSubDishes { subDishes ->
                    checkIngredientAmountChanges(item.value?.details ?: return@getSubDishes, subDishes)
                }
            }
        }
    }

    private fun getSubDishes(callback: (LinkedHashMap<String, IngredientDetails>) -> (Unit)) {
        val dbRef = Firebase.firestore.collection("ingredients-details").whereNotEqualTo("subIngredients", null)
        dbRef.get().addOnSuccessListener { snapshot ->
            val ingredients: LinkedHashMap<String, IngredientDetails> = LinkedHashMap()
            if (snapshot != null) {
                for (document in snapshot) {
                    val ingredient = document.toObject<IngredientDetails>()
                    ingredients[ingredient.id] = ingredient
                }
            }
            callback(ingredients)
        }
    }

    private fun checkIngredientAmountChanges(details: OrderDetails, subDishes: LinkedHashMap<String, IngredientDetails>) {
        val ingredientsToChange = getIngredientsToChange(details, subDishes)
        for (ingredientId in ingredientsToChange.keys) {
            Firebase.firestore.runTransaction { transaction ->
                var dbRef = Firebase.firestore.collection("ingredients-basic").document(ingredientId)
                val oldAmount = transaction.get(dbRef).getLong("amount")?.toInt() ?: 0
                val difference = ingredientsToChange[ingredientId]?.toInt()?.times(-1) ?: 0
                val newAmount = oldAmount + difference
                transaction.update(dbRef, "amount", newAmount)

                val newAmountChange =
                    IngredientAmountChange(
                        Firebase.auth.uid ?: return@runTransaction,
                        difference,
                        newAmount,
                        IngredientModificationType.ORDER.ordinal
                    )
                dbRef = Firebase.firestore.collection("ingredients-details").document(ingredientId)
                transaction.update(dbRef, "amountChanges.${newAmountChange.date.time}", newAmountChange)
            }
        }
    }

    private fun getIngredientsToChange(details: OrderDetails, subDishes: LinkedHashMap<String, IngredientDetails>): HashMap<String, BigDecimal> {
        val ingredients = HashMap<String, BigDecimal>()
        for (dish in details.dishes.values) {
            val multiplier = BigDecimal(dish.amount)
            val usedIngredients = dish.dish.details.baseIngredients.values.toMutableList()
            usedIngredients.addAll(dish.dish.details.otherIngredients.values.filter {
                !dish.unusedOtherIngredients.map { it.id }.toMutableList().contains(it.id)
            })
            usedIngredients.addAll(dish.dish.details.possibleIngredients.values.filter {
                dish.usedPossibleIngredients.map { it.id }.toMutableList().contains(it.id)
            })
            for (ingredient in usedIngredients) {
                if (subDishes[ingredient.id] == null) {
                    ingredients[ingredient.id] = ((ingredients[ingredient.id] ?: BigDecimal.ZERO) + BigDecimal(ingredient.amount)) * multiplier
                } else {
                    val subMultiplier = BigDecimal(ingredient.amount) * multiplier
                    for (subIngredient in subDishes[ingredient.id]?.subIngredients ?: ArrayList()) {
                        ingredients[subIngredient.id] =
                            ((ingredients[subIngredient.id] ?: BigDecimal.ZERO) + BigDecimal(subIngredient.amount)) * subMultiplier
                    }
                }
            }
        }
        return ingredients
    }

    fun updateDeliverer(newDelivererId: String) {
        Firebase.firestore.runTransaction { transaction ->
            transaction.update(dbRefDetails.document(itemId), "delivererId", newDelivererId)
            transaction.update(Firebase.firestore.collection("users-details").document(newDelivererId), "ordersToDeliver.$itemId", true)

            if (this.delivererId.isNotEmpty() && this.delivererId != newDelivererId) {
                transaction.update(Firebase.firestore.collection("users-details").document(this.delivererId), "ordersToDeliver.$itemId", null)
            }
        }.addOnSuccessListener {
            this.delivererId = newDelivererId
        }
    }

    fun getAllPossibleDeliverers() {
        Firebase.firestore.collection("users-basic").whereEqualTo("delivery", true).get().addOnSuccessListener { snapshot ->
            _possibleDeliverers.value = snapshot.mapNotNull { documentSnapshot -> documentSnapshot.toObject<UserBasic>() }.toMutableList()
        }
    }

    fun getUserName(userId: String) {
        Firebase.firestore.collection("users-basic").document(userId).get().addOnSuccessListener { snapshot ->
            val user = snapshot.toObject<UserBasic>() ?: UserBasic()
            _userName.value = user.getFullName()
        }
    }

    override fun isDisabled(): Boolean {
        return item.value?.basic?.disabled == true
    }
}
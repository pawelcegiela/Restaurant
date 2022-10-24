package pi.restaurant.management.model.fragments.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.model.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.objects.SnapshotsPair
import pi.restaurant.management.objects.data.ingredient.IngredientAmountChange
import pi.restaurant.management.objects.data.ingredient.IngredientDetails
import pi.restaurant.management.objects.data.order.Order
import pi.restaurant.management.objects.data.order.OrderBasic
import pi.restaurant.management.objects.data.order.OrderDetails
import pi.restaurant.management.objects.data.user.UserBasic
import pi.restaurant.management.objects.enums.IngredientModificationType
import pi.restaurant.management.objects.enums.OrderStatus
import pi.restaurant.management.utils.StringFormatUtils
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
        val basic = snapshotsPair.basic?.getValue<OrderBasic>() ?: OrderBasic()
        val details = snapshotsPair.details?.getValue<OrderDetails>() ?: OrderDetails()
        _item.value = Order(itemId, basic, details)
    }

    fun updateOrderStatus(newStatus: Int) {
        var databaseRef = Firebase.database.getReference(databasePath).child("basic").child(itemId).child("orderStatus")
        databaseRef.setValue(newStatus)
        _orderStatus.value = newStatus

        val time = Date().time.toString()
        databaseRef = Firebase.database.getReference(databasePath).child("details").child(itemId).child("statusChanges").child(time)
        databaseRef.setValue(newStatus)

        _statusChange.value = StringFormatUtils.formatDateTime(Date(time.toLong())) to newStatus

        if (newStatus == OrderStatus.ACCEPTED.ordinal) {
            getSubDishes { subDishes ->
                checkIngredientAmountChanges(item.value?.details ?: return@getSubDishes, subDishes)
            }
        }
    }

    private fun getSubDishes(callback: (LinkedHashMap<String, IngredientDetails>) -> (Unit)) {
        val databaseRef = Firebase.database.getReference("ingredients").child("details")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val map = dataSnapshot.getValue<HashMap<String, IngredientDetails>>() ?: HashMap()
                callback(map.filter { it.value.subIngredients != null } as LinkedHashMap<String, IngredientDetails>)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun checkIngredientAmountChanges(details: OrderDetails, subDishes: LinkedHashMap<String, IngredientDetails>) {
        val ingredientsToChange = getIngredientsToChange(details, subDishes)
        for (ingredientId in ingredientsToChange.keys) {
            var databaseRef = Firebase.database.getReference("ingredients").child("basic").child(ingredientId)
            databaseRef.child("amount").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val oldAmount = dataSnapshot.getValue<Int>() ?: return
                    val change = ingredientsToChange[ingredientId]?.toInt()?.times(-1) ?: 0
                    databaseRef.child("amount").setValue(oldAmount + change)
                    val newAmountChange =
                        IngredientAmountChange(Firebase.auth.uid ?: return, change, oldAmount + change, IngredientModificationType.ORDER.ordinal)
                    databaseRef = Firebase.database.getReference("ingredients").child("details").child(ingredientId).child("amountChanges")
                    databaseRef.child(newAmountChange.date.time.toString()).setValue(newAmountChange)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    private fun getIngredientsToChange(details: OrderDetails, subDishes: LinkedHashMap<String, IngredientDetails>): HashMap<String, BigDecimal> {
        // TODO Kwestia subdishów
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
        var databaseRef = Firebase.database.getReference(databasePath).child("details").child(itemId).child("delivererId")
        databaseRef.setValue(newDelivererId)

        databaseRef =
            Firebase.database.getReference("users").child("details").child(newDelivererId).child("ordersToDeliver").child(itemId)
        databaseRef.setValue(true)

        if (this.delivererId.isNotEmpty() && this.delivererId != newDelivererId) {
            databaseRef =
                Firebase.database.getReference("users").child("details").child(this.delivererId).child("ordersToDeliver")
                    .child(itemId)
            databaseRef.setValue(null)
        }
        this.delivererId = newDelivererId
    }

    fun getAllPossibleDeliverers() {
        val databaseRef = Firebase.database.getReference("users").child("basic")
        val query = databaseRef.orderByChild("delivery").equalTo(true)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val possibleDeliverersMap = dataSnapshot.getValue<HashMap<String, UserBasic>>() ?: HashMap()
                _possibleDeliverers.value = possibleDeliverersMap.map { it.value }.toMutableList()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun getUserName(userId: String) {
        val databaseRef = Firebase.database.getReference("users").child("basic").child(userId)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue<UserBasic>() ?: UserBasic()
                _userName.value = user.getFullName()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun isDisabled(): Boolean {
        return item.value?.basic?.disabled == true
    }
}
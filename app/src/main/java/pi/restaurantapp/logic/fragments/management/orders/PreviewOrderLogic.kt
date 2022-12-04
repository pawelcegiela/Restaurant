package pi.restaurantapp.logic.fragments.management.orders

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.logic.fragments.AbstractPreviewItemLogic
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.ingredient.IngredientAmountChange
import pi.restaurantapp.objects.data.ingredient.IngredientDetails
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.data.order.OrderDetails
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.enums.CollectionType
import pi.restaurantapp.objects.enums.IngredientModificationType
import pi.restaurantapp.objects.enums.OrderStatus
import java.math.BigDecimal
import java.util.*

class PreviewOrderLogic : AbstractPreviewItemLogic() {
    override val databasePath = "orders"

    fun updateOrderStatus(item: Order, closeWithoutRealization: Boolean, callback: (Int, Pair<String, Int>) -> Unit) {
        val newStatus = if (closeWithoutRealization) {
            OrderStatus.CLOSED_WITHOUT_REALIZATION.ordinal
        } else {
            OrderStatus.getNextStatusId(item.basic.orderStatus, CollectionType.values()[item.basic.collectionType])
        }
        if (newStatus == item.basic.orderStatus) {
            return
        }

        val time = Date().time
        if (newStatus == OrderStatus.ACCEPTED.ordinal) {
            getSubDishes { subDishes ->
                Firebase.firestore.runTransaction { transaction ->
                    checkIngredientAmountChanges(item.details, subDishes, transaction)
                    transaction.update(dbRefBasic.document(item.id), "orderStatus", newStatus)
                    transaction.update(dbRefDetails.document(item.id), "statusChanges.$time", newStatus)
                }.addOnSuccessListener {
                    callback(newStatus, StringFormatUtils.formatDateTime(Date(time)) to newStatus)
                }
            }
        } else {
            Firebase.firestore.runTransaction { transaction ->
                transaction.update(dbRefBasic.document(item.id), "orderStatus", newStatus)
                transaction.update(dbRefDetails.document(item.id), "statusChanges.$time", newStatus)
            }.addOnSuccessListener {
                callback(newStatus, StringFormatUtils.formatDateTime(Date(time)) to newStatus)
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

    private fun checkIngredientAmountChanges(details: OrderDetails, subDishes: LinkedHashMap<String, IngredientDetails>, transaction: Transaction) {
        val ingredientsToChange = getIngredientsToChange(details, subDishes)
        val newAmounts = HashMap<String, Int>()
        val newAmountChanges = HashMap<String, IngredientAmountChange>()

        val dbRefIngredientsBasic = Firebase.firestore.collection("ingredients-basic")
        val dbRefIngredientsDetails = Firebase.firestore.collection("ingredients-details")

        for (ingredientId in ingredientsToChange.keys) {
            val oldAmount = transaction.get(dbRefIngredientsBasic.document(ingredientId)).getLong("amount")?.toInt() ?: 0
            val difference = ingredientsToChange[ingredientId]?.toInt()?.times(-1) ?: 0
            val newAmount = oldAmount + difference
            val newAmountChange =
                IngredientAmountChange(
                    Firebase.auth.uid ?: return,
                    difference,
                    newAmount,
                    IngredientModificationType.ORDER.ordinal
                )

            newAmounts[ingredientId] = newAmount
            newAmountChanges[ingredientId] = newAmountChange
        }

        for (ingredientId in newAmounts.keys) {
            transaction.update(dbRefIngredientsBasic.document(ingredientId), "amount", newAmounts[ingredientId])
            transaction.update(
                dbRefIngredientsDetails.document(ingredientId),
                "amountChanges.${newAmountChanges[ingredientId]!!.date.time}",
                newAmountChanges[ingredientId]
            )
        }
    }

    private fun getIngredientsToChange(details: OrderDetails, subDishes: LinkedHashMap<String, IngredientDetails>): HashMap<String, BigDecimal> {
        val ingredients = HashMap<String, BigDecimal>()
        for (dish in details.dishes.values) {
            val multiplier = BigDecimal(dish.amount)
            val usedIngredients = dish.dish.details.baseIngredients.values.toMutableList()
            usedIngredients.addAll(dish.dish.details.otherIngredients.values.filter {
                !dish.unusedOtherIngredients.map { ingredient -> ingredient.id }.toMutableList().contains(it.id)
            })
            usedIngredients.addAll(dish.dish.details.possibleIngredients.values.filter {
                dish.usedPossibleIngredients.map { ingredient -> ingredient.id }.toMutableList().contains(it.id)
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

    fun updateDeliverer(itemId: String, oldDelivererId: String, newDelivererId: String, callback: () -> Unit) {
        Firebase.firestore.runTransaction { transaction ->
            transaction.update(dbRefDetails.document(itemId), "delivererId", newDelivererId)
            transaction.update(Firebase.firestore.collection("users-details").document(newDelivererId), "ordersToDeliver.$itemId", true)

            if (oldDelivererId.isNotEmpty() && oldDelivererId != newDelivererId) {
                transaction.update(Firebase.firestore.collection("users-details").document(oldDelivererId), "ordersToDeliver.$itemId", null)
            }
        }.addOnSuccessListener {
            callback()
        }
    }

    fun getAllPossibleDeliverers(callback: (MutableList<UserBasic>) -> Unit) {
        Firebase.firestore.collection("users-basic").whereEqualTo("delivery", true).get().addOnSuccessListener { snapshot ->
            callback(snapshot.mapNotNull { documentSnapshot -> documentSnapshot.toObject<UserBasic>() }.toMutableList())
        }
    }

    fun getUserName(userId: String, callback: (String) -> Unit) {
        Firebase.firestore.collection("users-basic").document(userId).get().addOnSuccessListener { snapshot ->
            val user = snapshot.toObject<UserBasic>() ?: UserBasic()
            callback(user.getFullName())
        }
    }
}
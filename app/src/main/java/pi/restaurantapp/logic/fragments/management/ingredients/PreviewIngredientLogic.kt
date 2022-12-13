package pi.restaurantapp.logic.fragments.management.ingredients

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.logic.fragments.AbstractPreviewItemLogic
import pi.restaurantapp.objects.data.ingredient.IngredientAmountChange
import pi.restaurantapp.objects.enums.IngredientModificationType

/**
 * Class responsible for business logic and communication with database (Model layer) for PreviewIngredientFragment.
 * @see pi.restaurantapp.ui.fragments.management.ingredients.PreviewIngredientFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.ingredients.PreviewIngredientViewModel ViewModel layer
 */
class PreviewIngredientLogic : AbstractPreviewItemLogic() {
    override val databasePath = "ingredients"

    fun getContainingDishes(
        containingDishesIds: List<String>,
        containingSubDishesIds: List<String>,
        callback: (ArrayList<String>, ArrayList<String>) -> Unit
    ) {
        val containingDishes = ArrayList<String>()
        val containingSubDishes = ArrayList<String>()

        if (containingDishesIds.isEmpty() && containingSubDishesIds.isEmpty()) {
            callback(containingDishes, containingSubDishes)
            return
        }

        for (id in containingDishesIds) {
            Firebase.firestore.collection("dishes-basic").document(id).get().addOnSuccessListener { snapshot ->
                containingDishes.add(snapshot.getString("name") ?: "")
                if (containingDishes.size == containingDishesIds.size && containingSubDishes.size == containingSubDishesIds.size) {
                    callback(containingDishes, containingSubDishes)
                }
            }
        }

        for (id in containingSubDishesIds) {
            Firebase.firestore.collection("ingredients-basic").document(id).get().addOnSuccessListener { snapshot ->
                containingSubDishes.add(snapshot.getString("name") ?: "")
                if (containingSubDishes.size == containingSubDishesIds.size && containingDishes.size == containingDishesIds.size) {
                    callback(containingDishes, containingSubDishes)
                }
            }
        }
    }

    fun updateIngredientAmount(
        id: String,
        _amount: Int,
        modificationType: IngredientModificationType,
        containingDishesIds: List<String>,
        shouldDisable: Boolean,
        setNewAmount: (Int) -> (Unit),
        addNewAmountChange: (IngredientAmountChange) -> (Unit)
    ) {
        var newAmountChange = IngredientAmountChange()
        Firebase.firestore.runTransaction { transaction ->
            val difference = _amount * (if (modificationType == IngredientModificationType.CORRECTION) -1 else 1)
            val oldAmount = transaction.get(dbRefBasic.document(id)).getLong("amount")?.toInt() ?: 0
            val newAmount = Integer.max(oldAmount + difference, 0)
            transaction.update(dbRefBasic.document(id), "amount", newAmount)

            newAmountChange = IngredientAmountChange(Firebase.auth.uid!!, difference, newAmount, modificationType.ordinal)
            transaction.update(dbRefDetails.document(id), "amountChanges.${newAmountChange.date.time}", newAmountChange)

            newAmount
        }.addOnSuccessListener { newAmount ->
            setNewAmount(newAmount)
            addNewAmountChange(newAmountChange)
            if (newAmount == 0 && shouldDisable) {
                disableDishes(containingDishesIds)
            }
        }
    }

    private fun disableDishes(containingDishesIds: List<String>) {
        Firebase.firestore.runTransaction { transaction ->
            for (id in containingDishesIds) {
                transaction.update(Firebase.firestore.collection("dishes-basic").document(id), "active", false)
            }
        }
    }
}
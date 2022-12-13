package pi.restaurantapp.logic.fragments.management.ingredients

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.logic.fragments.AbstractModifyItemLogic
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.ingredient.IngredientBasic
import pi.restaurantapp.objects.data.ingredient.IngredientDetails
import pi.restaurantapp.objects.data.ingredient.IngredientItem

/**
 * Abstract class responsible for business logic and communication with database (Model layer) for AbstractModifyIngredientFragment.
 * @see pi.restaurantapp.ui.fragments.management.ingredients.AbstractModifyIngredientFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.ingredients.AbstractModifyIngredientViewModel ViewModel layer
 */
abstract class AbstractModifyIngredientLogic : AbstractModifyItemLogic() {
    override val databasePath = "ingredients"

    fun saveToDatabase(data: SplitDataObject, previousSubIngredients: MutableList<IngredientItem>, callback: (Boolean) -> Unit) {
        val subIngredients = (data.details as IngredientDetails).subIngredients ?: ArrayList()

        Firebase.firestore.runTransaction { transaction ->
            for (subIngredient in previousSubIngredients.filter { previousSubIngredient -> previousSubIngredient.id !in subIngredients.map { it.id } }) {
                transaction.update(dbRefDetails.document(subIngredient.id), "containingSubDishes.${data.details.id}", null)
            }
            for (subIngredient in subIngredients) {
                transaction.update(dbRefDetails.document(subIngredient.id), "containingSubDishes.${data.details.id}", true)
            }

            saveDocumentToDatabase(data, transaction)
        }.addOnSuccessListener {
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }
    }

    fun getAllIngredients(callback: (List<IngredientBasic>) -> Unit) {
        dbRefBasic.get().addOnSuccessListener { snapshot ->
            callback(snapshot
                .mapNotNull { documentSnapshot -> documentSnapshot.toObject<IngredientBasic>() }
                .filter { ingredient -> !ingredient.subDish && !ingredient.disabled })
        }
    }

}
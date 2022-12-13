package pi.restaurantapp.logic.fragments.management.ingredients

import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.ingredient.Ingredient
import pi.restaurantapp.objects.data.ingredient.IngredientBasic
import pi.restaurantapp.objects.data.ingredient.IngredientDetails

/**
 * Class responsible for business logic and communication with database (Model layer) for EditIngredientFragment.
 * @see pi.restaurantapp.ui.fragments.management.ingredients.EditIngredientFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.ingredients.EditIngredientViewModel ViewModel layer
 */
class EditIngredientLogic : AbstractModifyIngredientLogic() {
    var item: Ingredient? = null

    override fun saveDocumentToDatabase(data: SplitDataObject, transaction: Transaction) {
        transaction.update(dbRefBasic.document(data.id), "name", (data.basic as IngredientBasic).name)
        transaction.update(
            dbRefDetails.document(data.id),
            "subIngredients", (data.details as IngredientDetails).subIngredients,
            "disableDishOnShortage", (data.details as IngredientDetails).disableDishOnShortage
        )
    }

    override fun removeAdditionalElements() {
        val details = item?.details ?: IngredientDetails()
        if (details.subIngredients?.isNotEmpty() == true) {
            removeFromIngredients(details.subIngredients!!.map { it.id }, details.id)
        }
    }

    private fun removeFromIngredients(ingredients: List<String>, dishId: String) {
        val dbRef = Firebase.firestore.collection("ingredients-details")
        for (ingredientId in ingredients) {
            dbRef.document(ingredientId).update("containingSubDishes.$dishId", null)
        }
    }
}
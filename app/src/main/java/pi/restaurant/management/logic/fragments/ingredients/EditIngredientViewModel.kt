package pi.restaurant.management.logic.fragments.ingredients

import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.objects.data.ingredient.IngredientDetails

class EditIngredientViewModel : AbstractModifyIngredientViewModel() {
    override fun removeAdditionalElements() {
        val details = snapshotsPair.details?.getValue<IngredientDetails>() ?: IngredientDetails()
        if (details.subIngredients?.isNotEmpty() == true) {
            removeFromIngredients(details.subIngredients!!.map { it.id }, details.id)
        }
    }

    private fun removeFromIngredients(ingredients: List<String>, dishId : String) {
        val databaseRef = Firebase.database.getReference("ingredients").child("details")
        for (ingredientId in ingredients) {
            databaseRef.child(ingredientId).child("containingSubDishes").child(dishId).setValue(null)
        }
    }
}
package pi.restaurant.management.fragments.dishes

import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.data.DishDetails

class EditDishViewModel : AbstractModifyDishViewModel() {
    override fun removeAdditionalElements() {
        val details = snapshotsPair.details?.getValue<DishDetails>() ?: DishDetails()
        if (details.allergens.isNotEmpty()) {
            removeFromAllergens(details.allergens.map { it.key }, details.id)
        }
        if (details.baseIngredients.isNotEmpty()) {
            removeFromIngredients(details.baseIngredients.map { it.key }, details.id)
        }
        if (details.otherIngredients.isNotEmpty()) {
            removeFromIngredients(details.otherIngredients.map { it.key }, details.id)
        }
        if (details.possibleIngredients.isNotEmpty()) {
            removeFromIngredients(details.possibleIngredients.map { it.key }, details.id)
        }
    }

    private fun removeFromAllergens(allergens: List<String>, dishId : String) {
        val databaseRef = Firebase.database.getReference("allergens").child("details")
        for (allergenId in allergens) {
            databaseRef.child(allergenId).child("containingDishes").child(dishId).setValue(null)
        }
    }

    private fun removeFromIngredients(ingredients: List<String>, dishId : String) {
        val databaseRef = Firebase.database.getReference("ingredients").child("details")
        for (ingredientId in ingredients) {
            databaseRef.child(ingredientId).child("containingDishes").child(dishId).setValue(null)
        }
    }
}
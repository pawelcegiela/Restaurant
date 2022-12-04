package pi.restaurantapp.logic.fragments.management.dishes

import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.dish.Dish
import pi.restaurantapp.objects.data.dish.DishDetails

class EditDishLogic : AbstractModifyDishLogic() {
    var item: Dish? = null

    override fun saveDocumentToDatabase(data: SplitDataObject, transaction: Transaction) {
        transaction.set(dbRefBasic.document(data.id), data.basic)
        val details = data.details as DishDetails
        val detailsToChange = hashMapOf<String, Any>(
            "description" to details.description,
            "recipe" to details.recipe,
            "baseIngredients" to details.baseIngredients,
            "otherIngredients" to details.otherIngredients,
            "possibleIngredients" to details.possibleIngredients,
            "allergens" to details.allergens,
            "amount" to details.amount,
            "unit" to details.unit
        )
        transaction.update(dbRefDetails.document(data.id), detailsToChange)
    }

    override fun removeAdditionalElements() {
        val details = item?.details ?: DishDetails()
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

    private fun removeFromAllergens(allergens: List<String>, dishId: String) {
        val dbRef = Firebase.firestore.collection("allergens-details")
        for (allergenId in allergens) {
            dbRef.document(allergenId).update("containingDishes.$dishId", null)
        }
    }

    private fun removeFromIngredients(ingredients: List<String>, dishId: String) {
        val dbRef = Firebase.firestore.collection("ingredients-details")
        for (ingredientId in ingredients) {
            dbRef.document(ingredientId).update("containingDishes.$dishId", null)
        }
    }
}
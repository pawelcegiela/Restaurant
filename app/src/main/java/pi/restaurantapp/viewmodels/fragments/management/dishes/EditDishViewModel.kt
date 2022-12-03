package pi.restaurantapp.viewmodels.fragments.management.dishes

import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.dish.Dish
import pi.restaurantapp.objects.data.dish.DishBasic
import pi.restaurantapp.objects.data.dish.DishDetails

class EditDishViewModel : AbstractModifyDishViewModel() {
    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<DishBasic>() ?: DishBasic()
        val details = snapshotsPair.details?.toObject<DishDetails>() ?: DishDetails()
        setItem(Dish(itemId, basic, details))

        observer.baseIngredients = details.baseIngredients.map { it.value }.toMutableList()
        observer.otherIngredients = details.otherIngredients.map { it.value }.toMutableList()
        observer.possibleIngredients = details.possibleIngredients.map { it.value }.toMutableList()
        observer.allergens = details.allergens.map { it.value }.toMutableList()
    }

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
        val details = item.value?.details ?: DishDetails()
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
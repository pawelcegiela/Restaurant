package pi.restaurantapp.logic.fragments.management.dishes

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.logic.fragments.AbstractModifyItemLogic
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.allergen.AllergenBasic
import pi.restaurantapp.objects.data.ingredient.IngredientBasic
import pi.restaurantapp.objects.data.ingredient.IngredientItem

abstract class AbstractModifyDishLogic : AbstractModifyItemLogic() {
    override val databasePath = "dishes"

    fun saveToDatabase(
        data: SplitDataObject,
        ingredients: ArrayList<IngredientItem>,
        previousIngredients: MutableList<IngredientItem>,
        allergens: List<AllergenBasic>,
        previousAllergens: MutableList<AllergenBasic>,
        callback: (Boolean) -> Unit
    ) {
        Firebase.firestore.runTransaction { transaction ->
            val dbRefIngredients = Firebase.firestore.collection("ingredients-details")
            for (ingredient in previousIngredients.filter { previousIngredient -> previousIngredient.id !in ingredients.map { it.id } }) {
                transaction.update(dbRefIngredients.document(ingredient.id), "containingDishes.${data.details.id}", null)
            }
            for (ingredient in ingredients) {
                transaction.update(dbRefIngredients.document(ingredient.id), "containingDishes.${data.details.id}", true)
            }

            val dbRefAllergens = Firebase.firestore.collection("allergens-details")
            for (allergen in previousAllergens.filter { previousAllergen -> previousAllergen.id !in allergens.map { it.id } }) {
                transaction.update(dbRefAllergens.document(allergen.id), "containingDishes.${data.details.id}", null)
            }
            for (allergen in allergens) {
                transaction.update(dbRefAllergens.document(allergen.id), "containingDishes.${data.details.id}", true)
            }

            saveDocumentToDatabase(data, transaction)
        }.addOnSuccessListener {
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }
    }

    fun getAllIngredients(callback: (MutableList<IngredientBasic>) -> Unit) {
        Firebase.firestore.collection("ingredients-basic").get().addOnSuccessListener { snapshot ->
            val allIngredients = snapshot
                .mapNotNull { documentSnapshot -> documentSnapshot.toObject<IngredientBasic>() }
                .filter { ingredient -> !ingredient.disabled }
                .toMutableList()
            callback(allIngredients)
        }
    }

    fun getAllAllergens(callback: (MutableList<AllergenBasic>) -> Unit) {
        Firebase.firestore.collection("allergens-basic").get().addOnSuccessListener { snapshot ->
            val allAllergens = snapshot
                .mapNotNull { documentSnapshot -> documentSnapshot.toObject<AllergenBasic>() }
                .filter { allergen -> !allergen.disabled }
                .toMutableList()
            callback(allAllergens)
        }
    }
}
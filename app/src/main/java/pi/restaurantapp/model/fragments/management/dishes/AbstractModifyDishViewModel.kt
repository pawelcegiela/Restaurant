package pi.restaurantapp.model.fragments.management.dishes

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.model.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.allergen.AllergenBasic
import pi.restaurantapp.objects.data.dish.Dish
import pi.restaurantapp.objects.data.dish.DishBasic
import pi.restaurantapp.objects.data.dish.DishDetails
import pi.restaurantapp.objects.data.ingredient.IngredientBasic
import pi.restaurantapp.objects.data.ingredient.IngredientItem

abstract class AbstractModifyDishViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "dishes"

    val liveAllIngredients = MutableLiveData<MutableList<IngredientBasic>>()
    val liveAllAllergens = MutableLiveData<MutableList<AllergenBasic>>()

    override fun saveToDatabase(data: SplitDataObject) {
        val previousIngredients = getPreviousIngredients()
        val ingredients = getCurrentIngredients(data.details as DishDetails)
        val previousAllergens = getPreviousItem().details.allergens.map { it.value }
        val allergens = (data.details as DishDetails).allergens.map { it.value }

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
            setSaveStatus(true)
        }
    }

    fun getPreviousItem(): Dish {
        if (this is EditDishViewModel) {
            return item.value ?: Dish(itemId, DishBasic(), DishDetails())
        }
        return Dish(itemId, DishBasic(), DishDetails())
    }

    private fun getPreviousIngredients(): ArrayList<IngredientItem> {
        val previousBaseIngredients = getPreviousItem().details.baseIngredients.map { it.value }
        val previousOtherIngredients = getPreviousItem().details.otherIngredients.map { it.value }
        val previousPossibleIngredients = getPreviousItem().details.possibleIngredients.map { it.value }

        val previousIngredients = ArrayList<IngredientItem>()
        previousIngredients.addAll(previousBaseIngredients)
        previousIngredients.addAll(previousOtherIngredients)
        previousIngredients.addAll(previousPossibleIngredients)

        return previousIngredients
    }

    private fun getCurrentIngredients(details: DishDetails): ArrayList<IngredientItem> {
        val baseIngredients = details.baseIngredients.map { it.value }
        val otherIngredients = details.otherIngredients.map { it.value }
        val possibleIngredients = details.possibleIngredients.map { it.value }

        val ingredients = ArrayList<IngredientItem>()
        ingredients.addAll(baseIngredients)
        ingredients.addAll(otherIngredients)
        ingredients.addAll(possibleIngredients)

        return ingredients
    }

    fun getAllIngredients() {
        Firebase.firestore.collection("ingredients-basic").get().addOnSuccessListener { snapshot ->
            liveAllIngredients.value = snapshot
                .mapNotNull { documentSnapshot -> documentSnapshot.toObject<IngredientBasic>() }
                .filter { ingredient -> !ingredient.disabled }
                .toMutableList()
        }
    }

    fun getAllAllergens() {
        Firebase.firestore.collection("allergens-basic").get().addOnSuccessListener { snapshot ->
            liveAllAllergens.value = snapshot
                .mapNotNull { documentSnapshot -> documentSnapshot.toObject<AllergenBasic>() }
                .filter { allergen -> !allergen.disabled }
                .toMutableList()
        }
    }
}
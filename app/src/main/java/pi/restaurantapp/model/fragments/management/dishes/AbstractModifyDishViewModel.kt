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
        updateIngredients(data.details as DishDetails)
        updateAllergens(data.details as DishDetails)
        super.saveToDatabase(data)
    }

    fun getPreviousItem(): Dish {
        if (this is EditDishViewModel) {
            return item.value ?: Dish(itemId, DishBasic(), DishDetails())
        }
        return Dish(itemId, DishBasic(), DishDetails())
    }

    private fun updateIngredients(details: DishDetails) {
        val previousBaseIngredients = getPreviousItem().details.baseIngredients.map { it.value }
        val previousOtherIngredients = getPreviousItem().details.otherIngredients.map { it.value }
        val previousPossibleIngredients = getPreviousItem().details.possibleIngredients.map { it.value }
        val previousIngredients = ArrayList<IngredientItem>()
        previousIngredients.addAll(previousBaseIngredients)
        previousIngredients.addAll(previousOtherIngredients)
        previousIngredients.addAll(previousPossibleIngredients)

        val baseIngredients = details.baseIngredients.map { it.value }
        val otherIngredients = details.otherIngredients.map { it.value }
        val possibleIngredients = details.possibleIngredients.map { it.value }
        val ingredients = ArrayList<IngredientItem>()
        ingredients.addAll(baseIngredients)
        ingredients.addAll(otherIngredients)
        ingredients.addAll(possibleIngredients)

        Firebase.firestore.runTransaction { transaction ->
            val dbRef = Firebase.firestore.collection("ingredients-details")
            for (ingredient in previousIngredients) {
                if (ingredient.id !in ingredients.map { it.id }) {
                    transaction.update(dbRef.document(ingredient.id), "containingDishes.${details.id}", null)
                }
            }

            for (ingredient in ingredients) {
                transaction.update(dbRef.document(ingredient.id), "containingDishes.${details.id}", true)
            }
        }
    }

    private fun updateAllergens(details: DishDetails) {
        val previousAllergens = getPreviousItem().details.allergens.map { it.value }
        val allergens = details.allergens.map { it.value }

        Firebase.firestore.runTransaction { transaction ->
            val dbRef = Firebase.firestore.collection("allergens-details")
            for (allergen in previousAllergens) {
                if (allergen.id !in allergens.map { it.id }) {
                    transaction.update(dbRef.document(allergen.id), "containingDishes.${details.id}", null)
                }
            }

            for (allergen in allergens) {
                transaction.update(dbRef.document(allergen.id), "containingDishes.${details.id}", true)
            }
        }
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
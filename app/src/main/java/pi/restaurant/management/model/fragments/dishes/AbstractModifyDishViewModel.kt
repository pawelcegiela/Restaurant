package pi.restaurant.management.model.fragments.dishes

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.model.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.objects.data.SplitDataObject
import pi.restaurant.management.objects.data.allergen.AllergenBasic
import pi.restaurant.management.objects.data.dish.Dish
import pi.restaurant.management.objects.data.dish.DishBasic
import pi.restaurant.management.objects.data.dish.DishDetails
import pi.restaurant.management.objects.data.ingredient.IngredientBasic
import pi.restaurant.management.objects.data.ingredient.IngredientItem

abstract class AbstractModifyDishViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "dishes"

    val liveAllIngredients = MutableLiveData<MutableList<IngredientBasic>>()
    val liveAllAllergens = MutableLiveData<MutableList<AllergenBasic>>()

    override fun saveToDatabase(data: SplitDataObject) {
        updateIngredients(data.details as DishDetails)
        updateAllergens(data.details as DishDetails)
        super.saveToDatabase(data)
    }

    fun getPreviousItem() : Dish {
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

        val databaseRef = Firebase.database.getReference("ingredients").child("details")

        for (ingredient in previousIngredients) {
            if (ingredient.id !in ingredients.map { it.id }) {
                databaseRef.child(ingredient.id).child("containingDishes").child(details.id).setValue(null)
            }
        }

        for (ingredient in ingredients) {
            databaseRef.child(ingredient.id).child("containingDishes").child(details.id).setValue(true)
        }
    }

    private fun updateAllergens(details: DishDetails) {
        val previousAllergens = getPreviousItem().details.allergens.map { it.value }
        val allergens = details.allergens.map { it.value }

        val databaseRef = Firebase.database.getReference("allergens").child("details")

        for (allergen in previousAllergens) {
            if (allergen.id !in allergens.map { it.id }) {
                databaseRef.child(allergen.id).child("containingDishes").child(details.id).setValue(null)
            }
        }

        for (allergen in allergens) {
            databaseRef.child(allergen.id).child("containingDishes").child(details.id).setValue(true)
        }
    }

    fun getAllIngredients() {
        val databaseRef = Firebase.database.getReference("ingredients").child("basic")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue<HashMap<String, IngredientBasic>>() ?: HashMap()
                liveAllIngredients.value = data.toList().map { it.second }.filter { !it.disabled }.toMutableList()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun getAllAllergens() {
        val databaseRef = Firebase.database.getReference("allergens").child("basic")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue<HashMap<String, AllergenBasic>>() ?: HashMap()
                liveAllAllergens.value = data.toList().map { it.second }.filter { !it.disabled }.toMutableList()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
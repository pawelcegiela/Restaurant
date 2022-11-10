package pi.restaurantapp.model.fragments.management.ingredients

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.model.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.ingredient.Ingredient
import pi.restaurantapp.objects.data.ingredient.IngredientBasic
import pi.restaurantapp.objects.data.ingredient.IngredientDetails

abstract class AbstractModifyIngredientViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "ingredients"

    val liveAllIngredients = MutableLiveData<MutableList<IngredientBasic>>()

    override fun saveToDatabase(data: SplitDataObject) {
        updateSubIngredients(data.details as IngredientDetails)
        super.saveToDatabase(data)
    }

    fun getPreviousItem(): Ingredient {
        if (this is EditIngredientViewModel) {
            return item.value ?: Ingredient(itemId, IngredientBasic(), IngredientDetails())
        }
        return Ingredient(itemId, IngredientBasic(), IngredientDetails())
    }

    private fun updateSubIngredients(details: IngredientDetails) {
        val previousSubIngredients = getPreviousItem().details.subIngredients ?: ArrayList()
        val subIngredients = details.subIngredients ?: ArrayList()

        Firebase.firestore.runTransaction { transaction ->
            for (subIngredient in previousSubIngredients) {
                if (subIngredient.id !in subIngredients.map { it.id }) {
                    transaction.update(dbRefDetails.document(subIngredient.id), "containingSubDishes.${details.id}", null)
                }
            }

            for (subIngredient in subIngredients) {
                transaction.update(dbRefDetails.document(subIngredient.id), "containingSubDishes.${details.id}", true)
            }
        }
    }

    fun getAllIngredients() {
        dbRefBasic.get().addOnSuccessListener { snapshot ->
            liveAllIngredients.value = snapshot
                .mapNotNull { documentSnapshot -> documentSnapshot.toObject<IngredientBasic>() }
                .filter { ingredient -> !ingredient.subDish && !ingredient.disabled }
                .toMutableList()
        }
    }

}
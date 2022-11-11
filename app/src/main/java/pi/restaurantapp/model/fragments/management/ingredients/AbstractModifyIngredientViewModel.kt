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
        val previousSubIngredients = getPreviousItem().details.subIngredients ?: ArrayList()
        val subIngredients = (data.details as IngredientDetails).subIngredients ?: ArrayList()

        Firebase.firestore.runTransaction { transaction ->
            for (subIngredient in previousSubIngredients.filter { previousSubIngredient -> previousSubIngredient.id !in subIngredients.map { it.id } }) {
                transaction.update(dbRefDetails.document(subIngredient.id), "containingSubDishes.${data.details.id}", null)
            }
            for (subIngredient in subIngredients) {
                transaction.update(dbRefDetails.document(subIngredient.id), "containingSubDishes.${data.details.id}", true)
            }

            saveDocumentToDatabase(data, transaction)
        }.addOnSuccessListener {
            setSaveStatus(true)
        }
    }

    fun getPreviousItem(): Ingredient {
        if (this is EditIngredientViewModel) {
            return item.value ?: Ingredient(itemId, IngredientBasic(), IngredientDetails())
        }
        return Ingredient(itemId, IngredientBasic(), IngredientDetails())
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
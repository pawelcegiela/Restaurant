package pi.restaurant.management.model.fragments.management.ingredients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.objects.SnapshotsPair
import pi.restaurant.management.objects.data.ingredient.Ingredient
import pi.restaurant.management.objects.data.ingredient.IngredientBasic
import pi.restaurant.management.objects.data.ingredient.IngredientDetails

class EditIngredientViewModel : AbstractModifyIngredientViewModel() {
    private val _item: MutableLiveData<Ingredient> = MutableLiveData()
    val item: LiveData<Ingredient> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.getValue<IngredientBasic>() ?: IngredientBasic()
        val details = snapshotsPair.details?.getValue<IngredientDetails>() ?: IngredientDetails()
        _item.value = Ingredient(itemId, basic, details)
    }

    override fun removeAdditionalElements() {
        val details = item.value?.details ?: IngredientDetails()
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
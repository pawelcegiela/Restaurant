package pi.restaurantapp.model.fragments.management.ingredients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.ingredient.Ingredient
import pi.restaurantapp.objects.data.ingredient.IngredientBasic
import pi.restaurantapp.objects.data.ingredient.IngredientDetails

class EditIngredientViewModel : AbstractModifyIngredientViewModel() {
    private val _item: MutableLiveData<Ingredient> = MutableLiveData()
    val item: LiveData<Ingredient> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<IngredientBasic>() ?: IngredientBasic()
        val details = snapshotsPair.details?.toObject<IngredientDetails>() ?: IngredientDetails()
        _item.value = Ingredient(itemId, basic, details)
    }

    override fun removeAdditionalElements() {
        val details = item.value?.details ?: IngredientDetails()
        if (details.subIngredients?.isNotEmpty() == true) {
            removeFromIngredients(details.subIngredients!!.map { it.id }, details.id)
        }
    }

    private fun removeFromIngredients(ingredients: List<String>, dishId: String) {
        val dbRef = Firebase.firestore.collection("ingredients-details")
        for (ingredientId in ingredients) {
            dbRef.document(ingredientId).update("containingSubDishes.$dishId", null)
        }
    }
}
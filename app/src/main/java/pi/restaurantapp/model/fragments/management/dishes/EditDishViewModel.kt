package pi.restaurantapp.model.fragments.management.dishes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.dish.Dish
import pi.restaurantapp.objects.data.dish.DishBasic
import pi.restaurantapp.objects.data.dish.DishDetails

class EditDishViewModel : AbstractModifyDishViewModel() {
    private val _item: MutableLiveData<Dish> = MutableLiveData()
    val item: LiveData<Dish> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<DishBasic>() ?: DishBasic()
        val details = snapshotsPair.details?.toObject<DishDetails>() ?: DishDetails()
        _item.value = Dish(itemId, basic, details)
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
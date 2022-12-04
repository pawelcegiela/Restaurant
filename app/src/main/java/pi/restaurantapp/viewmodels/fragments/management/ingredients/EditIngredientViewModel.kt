package pi.restaurantapp.viewmodels.fragments.management.ingredients

import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.management.ingredients.EditIngredientLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.ingredient.Ingredient
import pi.restaurantapp.objects.data.ingredient.IngredientBasic
import pi.restaurantapp.objects.data.ingredient.IngredientDetails

class EditIngredientViewModel : AbstractModifyIngredientViewModel() {
    override val logic = EditIngredientLogic()

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<IngredientBasic>() ?: IngredientBasic()
        val details = snapshotsPair.details?.toObject<IngredientDetails>() ?: IngredientDetails()
        setItem(Ingredient(itemId, basic, details))
        logic.item = Ingredient(itemId, basic, details)

        observer.subIngredients = item.value?.details?.subIngredients ?: ArrayList()
    }
}
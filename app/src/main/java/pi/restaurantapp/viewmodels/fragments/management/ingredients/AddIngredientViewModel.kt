package pi.restaurantapp.viewmodels.fragments.management.ingredients

import pi.restaurantapp.objects.data.ingredient.Ingredient
import pi.restaurantapp.objects.data.ingredient.IngredientBasic
import pi.restaurantapp.objects.data.ingredient.IngredientDetails
import pi.restaurantapp.logic.utils.StringFormatUtils

class AddIngredientViewModel : AbstractModifyIngredientViewModel() {
    override fun createItem() {
        itemId = StringFormatUtils.formatId()
        setItem(Ingredient(itemId, IngredientBasic(itemId), IngredientDetails(itemId)))
        setReadyToInitialize()
    }
}
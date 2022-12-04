package pi.restaurantapp.viewmodels.fragments.management.ingredients

import pi.restaurantapp.logic.fragments.management.ingredients.AddIngredientLogic
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.ingredient.Ingredient
import pi.restaurantapp.objects.data.ingredient.IngredientBasic
import pi.restaurantapp.objects.data.ingredient.IngredientDetails

class AddIngredientViewModel : AbstractModifyIngredientViewModel() {
    override val logic = AddIngredientLogic()

    override fun createItem() {
        itemId = StringFormatUtils.formatId()
        setItem(Ingredient(itemId, IngredientBasic(itemId), IngredientDetails(itemId)))
        setReadyToInitialize()
    }
}
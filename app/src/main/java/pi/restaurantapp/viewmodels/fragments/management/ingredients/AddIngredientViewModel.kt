package pi.restaurantapp.viewmodels.fragments.management.ingredients

import pi.restaurantapp.logic.fragments.management.ingredients.AddIngredientLogic
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.ingredient.Ingredient
import pi.restaurantapp.objects.data.ingredient.IngredientBasic
import pi.restaurantapp.objects.data.ingredient.IngredientDetails
import pi.restaurantapp.objects.enums.ToolbarType

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for AddIngredientFragment.
 * @see pi.restaurantapp.logic.fragments.management.ingredients.AddIngredientLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.ingredients.AddIngredientFragment View layer
 */
class AddIngredientViewModel : AbstractModifyIngredientViewModel() {
    override val logic = AddIngredientLogic()

    override fun createItem() {
        itemId = StringFormatUtils.formatId()
        setItem(Ingredient(itemId, IngredientBasic(itemId), IngredientDetails(itemId)))
        setReadyToUnlock()

        toolbarType.value = ToolbarType.SAVE
    }
}
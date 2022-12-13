package pi.restaurantapp.viewmodels.fragments.management.dishes

import pi.restaurantapp.logic.fragments.management.dishes.AddDishLogic
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.dish.Dish
import pi.restaurantapp.objects.data.dish.DishBasic
import pi.restaurantapp.objects.data.dish.DishDetails
import pi.restaurantapp.objects.enums.ToolbarType

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for AddDishFragment.
 * @see pi.restaurantapp.logic.fragments.management.dishes.AddDishLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.dishes.AddDishFragment View layer
 */
class AddDishViewModel : AbstractModifyDishViewModel() {
    override val logic = AddDishLogic()

    override fun createItem() {
        itemId = StringFormatUtils.formatId()
        setItem(Dish(itemId, DishBasic(itemId), DishDetails(itemId)))
        setReadyToUnlock()

        toolbarType.value = ToolbarType.SAVE
    }
}
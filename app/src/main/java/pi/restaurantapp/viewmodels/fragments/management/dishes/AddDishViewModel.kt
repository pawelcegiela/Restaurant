package pi.restaurantapp.viewmodels.fragments.management.dishes

import pi.restaurantapp.logic.fragments.management.dishes.AddDishLogic
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.dish.Dish
import pi.restaurantapp.objects.data.dish.DishBasic
import pi.restaurantapp.objects.data.dish.DishDetails
import pi.restaurantapp.objects.enums.ToolbarType

class AddDishViewModel : AbstractModifyDishViewModel() {
    override val logic = AddDishLogic()

    override fun createItem() {
        itemId = StringFormatUtils.formatId()
        setItem(Dish(itemId, DishBasic(itemId), DishDetails(itemId)))
        setReadyToInitialize()

        toolbarType.value = ToolbarType.SAVE
    }
}
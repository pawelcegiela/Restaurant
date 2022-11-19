package pi.restaurantapp.model.fragments.management.dishes

import pi.restaurantapp.objects.data.dish.Dish
import pi.restaurantapp.objects.data.dish.DishBasic
import pi.restaurantapp.objects.data.dish.DishDetails
import pi.restaurantapp.utils.StringFormatUtils

class AddDishViewModel : AbstractModifyDishViewModel() {
    override fun createItem() {
        itemId = StringFormatUtils.formatId()
        setItem(Dish(itemId, DishBasic(itemId), DishDetails(itemId)))
        setReadyToInitialize()
    }
}
package pi.restaurantapp.viewmodels.fragments.management.ingredients

import pi.restaurantapp.logic.fragments.management.ingredients.IngredientsMainLogic
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel

class IngredientsMainViewModel : AbstractItemListViewModel() {
    override val logic = IngredientsMainLogic()
}
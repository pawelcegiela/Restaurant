package pi.restaurantapp.viewmodels.fragments.management.dishes

import pi.restaurantapp.logic.fragments.management.dishes.DishesMainLogic
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel

class DishesMainViewModel : AbstractItemListViewModel() {
    override val logic = DishesMainLogic()

    var shouldDisplayFAB: Boolean = true

    override fun displayFAB(): Boolean {
        return shouldDisplayFAB
    }
}
package pi.restaurantapp.viewmodels.fragments.management.dishes

import pi.restaurantapp.logic.fragments.management.dishes.DishesMainLogic
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for DishesMainFragment.
 * @see pi.restaurantapp.logic.fragments.management.dishes.DishesMainLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.dishes.DishesMainFragment View layer
 */
class DishesMainViewModel : AbstractItemListViewModel() {
    override val logic = DishesMainLogic()

    var shouldDisplayFAB: Boolean = true

    override fun displayFAB(): Boolean {
        return shouldDisplayFAB
    }
}
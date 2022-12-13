package pi.restaurantapp.viewmodels.fragments.management.discounts

import pi.restaurantapp.logic.fragments.management.discounts.DiscountsMainLogic
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for DiscountsMainFragment.
 * @see pi.restaurantapp.logic.fragments.management.discounts.DiscountsMainLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.discounts.DiscountsMainFragment View layer
 */
class DiscountsMainViewModel : AbstractItemListViewModel() {
    override val logic = DiscountsMainLogic()
}
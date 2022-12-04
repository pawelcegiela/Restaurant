package pi.restaurantapp.viewmodels.fragments.management.discounts

import pi.restaurantapp.logic.fragments.management.discounts.DiscountsMainLogic
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel

class DiscountsMainViewModel : AbstractItemListViewModel() {
    override val logic = DiscountsMainLogic()
}
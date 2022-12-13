package pi.restaurantapp.viewmodels.fragments.management.orders

import pi.restaurantapp.logic.fragments.management.orders.OrdersMainLogic
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for OrdersMainFragment.
 * @see pi.restaurantapp.logic.fragments.management.orders.OrdersMainLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.orders.OrdersMainFragment View layer
 */
class OrdersMainViewModel : AbstractItemListViewModel() {
    override val logic = OrdersMainLogic()
}
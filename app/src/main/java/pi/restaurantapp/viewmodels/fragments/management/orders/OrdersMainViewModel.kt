package pi.restaurantapp.viewmodels.fragments.management.orders

import pi.restaurantapp.logic.fragments.management.orders.OrdersMainLogic
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel

class OrdersMainViewModel : AbstractItemListViewModel() {
    override val logic = OrdersMainLogic()
}
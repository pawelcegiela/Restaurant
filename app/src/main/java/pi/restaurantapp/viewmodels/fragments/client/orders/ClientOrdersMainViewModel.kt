package pi.restaurantapp.viewmodels.fragments.client.orders

import pi.restaurantapp.logic.fragments.client.orders.ClientOrdersMainLogic
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel

class ClientOrdersMainViewModel : AbstractItemListViewModel() {
    override val logic = ClientOrdersMainLogic()
}
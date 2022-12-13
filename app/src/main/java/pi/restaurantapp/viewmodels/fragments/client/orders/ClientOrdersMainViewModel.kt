package pi.restaurantapp.viewmodels.fragments.client.orders

import pi.restaurantapp.logic.fragments.client.orders.ClientOrdersMainLogic
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for ClientOrdersMainFragment.
 * @see pi.restaurantapp.logic.fragments.client.orders.ClientOrdersMainLogic Model layer
 * @see pi.restaurantapp.ui.fragments.client.orders.ClientOrdersMainFragment View layer
 */
class ClientOrdersMainViewModel : AbstractItemListViewModel() {
    override val logic = ClientOrdersMainLogic()
}
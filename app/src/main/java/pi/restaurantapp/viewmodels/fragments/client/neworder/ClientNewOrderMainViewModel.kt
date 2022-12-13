package pi.restaurantapp.viewmodels.fragments.client.neworder

import pi.restaurantapp.logic.fragments.client.neworder.ClientNewOrderMainLogic
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for ClientNewOrderMainFragment.
 * @see pi.restaurantapp.logic.fragments.client.neworder.ClientNewOrderMainLogic Model layer
 * @see pi.restaurantapp.ui.fragments.client.neworder.ClientNewOrderMainFragment View layer
 */
class ClientNewOrderMainViewModel : AbstractItemListViewModel() {
    override val logic = ClientNewOrderMainLogic()
    var shouldDisplayFAB: Boolean = true

    override fun displayFAB(): Boolean {
        return shouldDisplayFAB
    }
}
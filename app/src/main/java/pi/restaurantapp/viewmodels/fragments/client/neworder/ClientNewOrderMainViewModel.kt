package pi.restaurantapp.viewmodels.fragments.client.neworder

import pi.restaurantapp.logic.fragments.client.neworder.ClientNewOrderMainLogic
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel

class ClientNewOrderMainViewModel : AbstractItemListViewModel() {
    override val logic = ClientNewOrderMainLogic()
    var shouldDisplayFAB: Boolean = true

    override fun displayFAB(): Boolean {
        return shouldDisplayFAB
    }
}
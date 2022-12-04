package pi.restaurantapp.viewmodels.fragments.management.customers

import pi.restaurantapp.logic.fragments.management.customers.CustomersMainLogic
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel

class CustomersMainViewModel : AbstractItemListViewModel() {
    override val logic = CustomersMainLogic()

    override fun displayFAB() = false

}
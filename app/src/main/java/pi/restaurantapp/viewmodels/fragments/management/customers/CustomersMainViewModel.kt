package pi.restaurantapp.viewmodels.fragments.management.customers

import pi.restaurantapp.logic.fragments.management.customers.CustomersMainLogic
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for CustomersMainFragment.
 * @see pi.restaurantapp.logic.fragments.management.customers.CustomersMainLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.customers.CustomersMainFragment View layer
 */
class CustomersMainViewModel : AbstractItemListViewModel() {
    override val logic = CustomersMainLogic()

    override fun displayFAB() = false

}
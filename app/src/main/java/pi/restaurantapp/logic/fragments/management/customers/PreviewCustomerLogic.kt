package pi.restaurantapp.logic.fragments.management.customers

import pi.restaurantapp.logic.fragments.AbstractPreviewItemLogic

/**
 * Class responsible for business logic and communication with database (Model layer) for PreviewCustomerFragment.
 * @see pi.restaurantapp.ui.fragments.management.customers.PreviewCustomerFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.customers.PreviewCustomerViewModel ViewModel layer
 */
class PreviewCustomerLogic : AbstractPreviewItemLogic() {
    override val databasePath = "users"
}
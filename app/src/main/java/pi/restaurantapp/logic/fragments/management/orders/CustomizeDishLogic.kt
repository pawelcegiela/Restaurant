package pi.restaurantapp.logic.fragments.management.orders

import pi.restaurantapp.logic.fragments.AbstractPreviewItemLogic

/**
 * Class responsible for business logic and communication with database (Model layer) for CustomizeDishFragment.
 * @see pi.restaurantapp.ui.fragments.management.orders.CustomizeDishFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.orders.CustomizeDishObserver ViewModel layer
 */
class CustomizeDishLogic : AbstractPreviewItemLogic() {
    override val databasePath = "dishes"

}
package pi.restaurantapp.logic.fragments.management.restaurantdata

import pi.restaurantapp.logic.fragments.AbstractModifyItemLogic

/**
 * Class responsible for business logic and communication with database (Model layer) for EditDeliveryFragment.
 * @see pi.restaurantapp.ui.fragments.management.restaurantdata.EditDeliveryFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.restaurantdata.EditDeliveryViewModel ViewModel layer
 */
class EditDeliveryLogic : AbstractModifyItemLogic() {
    override val databasePath = "restaurantData"
}
package pi.restaurantapp.logic.fragments.management.restaurantdata

import pi.restaurantapp.logic.fragments.AbstractModifyItemLogic

/**
 * Class responsible for business logic and communication with database (Model layer) for EditAboutRestaurantFragment.
 * @see pi.restaurantapp.ui.fragments.management.restaurantdata.EditAboutRestaurantFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.restaurantdata.EditAboutRestaurantViewModel ViewModel layer
 */
class EditAboutRestaurantLogic : AbstractModifyItemLogic() {
    override val databasePath = "restaurantData"
}
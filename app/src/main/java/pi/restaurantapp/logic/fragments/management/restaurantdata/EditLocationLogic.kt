package pi.restaurantapp.logic.fragments.management.restaurantdata

import pi.restaurantapp.logic.fragments.AbstractModifyItemLogic

/**
 * Class responsible for business logic and communication with database (Model layer) for EditLocationFragment.
 * @see pi.restaurantapp.ui.fragments.management.restaurantdata.EditLocationFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.restaurantdata.EditLocationViewModel ViewModel layer
 */
class EditLocationLogic : AbstractModifyItemLogic() {
    override val databasePath = "restaurantData"
}
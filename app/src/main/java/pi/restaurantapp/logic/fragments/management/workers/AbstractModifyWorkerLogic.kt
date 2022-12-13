package pi.restaurantapp.logic.fragments.management.workers

import pi.restaurantapp.logic.fragments.AbstractModifyItemLogic

/**
 * Abstract class responsible for business logic and communication with database (Model layer) for AbstractModifyWorkerFragment.
 * @see pi.restaurantapp.ui.fragments.management.workers.AbstractModifyWorkerFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.workers.AbstractModifyWorkerViewModel ViewModel layer
 */
abstract class AbstractModifyWorkerLogic : AbstractModifyItemLogic() {
    override val databasePath = "users"
}
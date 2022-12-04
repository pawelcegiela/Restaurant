package pi.restaurantapp.logic.fragments.management.workers

import pi.restaurantapp.logic.fragments.AbstractModifyItemLogic

abstract class AbstractModifyWorkerLogic : AbstractModifyItemLogic() {
    override val databasePath = "users"

}
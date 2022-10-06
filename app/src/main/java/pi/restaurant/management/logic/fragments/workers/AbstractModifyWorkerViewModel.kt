package pi.restaurant.management.logic.fragments.workers

import pi.restaurant.management.logic.fragments.AbstractModifyItemViewModel

abstract class AbstractModifyWorkerViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "users"
}
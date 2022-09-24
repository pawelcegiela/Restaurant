package pi.restaurant.management.fragments.workers

import pi.restaurant.management.fragments.AbstractModifyItemViewModel

abstract class AbstractModifyWorkerViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "users"
}
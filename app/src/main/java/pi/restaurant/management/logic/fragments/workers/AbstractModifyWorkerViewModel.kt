package pi.restaurant.management.logic.fragments.workers

import pi.restaurant.management.logic.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.objects.data.user.User

abstract class AbstractModifyWorkerViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "users"
    var user: User? = null
}
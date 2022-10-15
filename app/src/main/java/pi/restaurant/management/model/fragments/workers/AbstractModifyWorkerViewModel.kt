package pi.restaurant.management.model.fragments.workers

import pi.restaurant.management.model.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.objects.data.user.User
import pi.restaurant.management.objects.data.user.UserBasic
import pi.restaurant.management.objects.data.user.UserDetails

abstract class AbstractModifyWorkerViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "users"

    fun getPreviousItem() : User {
        if (this is EditWorkerViewModel) {
            return item.value ?: User(itemId, UserBasic(), UserDetails())
        }
        return User()
    }
}
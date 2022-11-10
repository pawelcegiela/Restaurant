package pi.restaurantapp.model.fragments.management.workers

import pi.restaurantapp.model.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.objects.data.user.User
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.data.user.UserDetails

abstract class AbstractModifyWorkerViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "users"

    fun getPreviousItem() : User {
        if (this is EditWorkerViewModel) {
            return item.value ?: User(itemId, UserBasic(), UserDetails())
        }
        return User()
    }
}
package pi.restaurantapp.model.fragments.management.workers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.model.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.objects.data.user.User

abstract class AbstractModifyWorkerViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "users"

    private val _item: MutableLiveData<User> = MutableLiveData()
    val item: LiveData<User> = _item

    var isMyData : Boolean = false

    fun setItem(newItem: User) {
        _item.value = newItem
    }
}
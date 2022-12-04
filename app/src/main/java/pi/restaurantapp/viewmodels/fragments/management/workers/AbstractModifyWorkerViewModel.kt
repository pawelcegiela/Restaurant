package pi.restaurantapp.viewmodels.fragments.management.workers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.objects.data.user.User
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel

abstract class AbstractModifyWorkerViewModel : AbstractModifyItemViewModel() {

    private val _item: MutableLiveData<User> = MutableLiveData()
    val item: LiveData<User> = _item

    var isMyData: Boolean = false

    fun setItem(newItem: User) {
        _item.value = newItem
    }
}
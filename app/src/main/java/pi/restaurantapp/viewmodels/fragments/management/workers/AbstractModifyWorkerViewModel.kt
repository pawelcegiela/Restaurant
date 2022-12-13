package pi.restaurantapp.viewmodels.fragments.management.workers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.logic.utils.PreconditionUtils
import pi.restaurantapp.objects.data.NullableSplitDataObject
import pi.restaurantapp.objects.data.user.User
import pi.restaurantapp.objects.enums.Precondition
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel

/**
 * Abstract class responsible for presentation logic and binding between data/model and view (ViewModel layer) for AbstractModifyWorkerFragment.
 * @see pi.restaurantapp.logic.fragments.management.workers.AbstractModifyWorkerLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.workers.AbstractModifyWorkerFragment View layer
 */
abstract class AbstractModifyWorkerViewModel : AbstractModifyItemViewModel() {

    private val _item: MutableLiveData<User> = MutableLiveData()
    val item: LiveData<User> = _item

    override val splitDataObject get() = NullableSplitDataObject(itemId, item.value?.basic, item.value?.details)

    val userPassword: MutableLiveData<String> = MutableLiveData()
    val repeatUserPassword: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()

    var isMyData: Boolean = false

    fun setItem(newItem: User) {
        _item.value = newItem
    }

    override fun checkSavePreconditions(): Precondition {
        if (super.checkSavePreconditions() != Precondition.OK) {
            return super.checkSavePreconditions()
        }
        return PreconditionUtils.compareRoles(userRole.value!!, (item.value!!.basic).role)
    }
}
package pi.restaurantapp.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pi.restaurantapp.logic.fragments.AbstractFragmentLogic
import pi.restaurantapp.objects.enums.Role

/**
 * Abstract base class responsible for presentation logic and binding between data/model and view (ViewModel layer).
 * @see pi.restaurantapp.logic.fragments.AbstractFragmentLogic Model layer
 */
abstract class AbstractFragmentViewModel : ViewModel() {
    abstract val logic: AbstractFragmentLogic

    private val _userRole: MutableLiveData<Int> = MutableLiveData(Role.getPlaceholder())
    val userRole: LiveData<Int> = _userRole

    open fun getUserRole() {
        logic.getUserRole { role ->
            _userRole.value = role
        }
    }

    fun setUserRole(role: Int) {
        _userRole.value = role
    }
}
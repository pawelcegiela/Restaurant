package pi.restaurantapp.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pi.restaurantapp.logic.fragments.AbstractFragmentLogic
import pi.restaurantapp.objects.enums.Role

abstract class AbstractFragmentViewModel : ViewModel() {
    abstract val logic: AbstractFragmentLogic

    private val _userRole: MutableLiveData<Int> = MutableLiveData(Role.getPlaceholder())
    val userRole: LiveData<Int> = _userRole

    fun getUserRole() {
        logic.getUserRole { role ->
            _userRole.value = role
        }
    }
}
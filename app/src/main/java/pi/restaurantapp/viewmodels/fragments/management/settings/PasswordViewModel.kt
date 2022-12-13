package pi.restaurantapp.viewmodels.fragments.management.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pi.restaurantapp.logic.fragments.management.settings.PasswordLogic

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for PasswordFragment.
 * @see pi.restaurantapp.logic.fragments.management.settings.PasswordLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.settings.PasswordFragment View layer
 */
class PasswordViewModel : ViewModel() {
    private val logic = PasswordLogic()

    private val _currentPassword = MutableLiveData<String>()
    val currentPassword: LiveData<String> = _currentPassword

    private val _newPassword = MutableLiveData<String>()
    val newPassword: LiveData<String> = _newPassword

    private val _repeatNewPassword = MutableLiveData<String>()
    val repeatNewPassword: LiveData<String> = _repeatNewPassword

    private val _messageId = MutableLiveData<Int>()
    val messageId: LiveData<Int> = _messageId

    fun changePassword(oldPassword: String, newPassword: String, newPasswordRepeated: String) {
        logic.changePassword(oldPassword, newPassword, newPasswordRepeated, callbackMessage = { messageId ->
            _messageId.value = messageId
        }, callbackErase = {
            _currentPassword.value = ""
            _newPassword.value = ""
            _repeatNewPassword.value = ""
        })
    }

}
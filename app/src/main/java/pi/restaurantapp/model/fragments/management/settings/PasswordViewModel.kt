package pi.restaurantapp.model.fragments.management.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.R

class PasswordViewModel : ViewModel() {
    val liveCurrentPassword = MutableLiveData<String>()
    val liveNewPassword = MutableLiveData<String>()
    val liveRepeatNewPassword = MutableLiveData<String>()
    val messageId = MutableLiveData<Int>()

    fun changePassword(oldPassword: String, newPassword: String, newPasswordRepeated: String) {
        val user = Firebase.auth.currentUser ?: return

        liveCurrentPassword.value = ""

        if (newPassword != newPasswordRepeated) {
            messageId.value = R.string.passwords_differ
        } else if (user.email != null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)
            liveNewPassword.value = ""
            liveRepeatNewPassword.value = ""

            user.reauthenticate(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        user.updatePassword(newPassword).addOnCompleteListener {
                            messageId.value = R.string.password_changed
                        }
                    } else {
                        messageId.value = R.string.auth_failed_try_again
                    }
                }
        }
    }

}
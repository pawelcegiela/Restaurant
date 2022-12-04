package pi.restaurantapp.logic.fragments.management.settings

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.R

class PasswordLogic {
    fun changePassword(
        oldPassword: String,
        newPassword: String,
        newPasswordRepeated: String,
        callbackMessage: (Int) -> Unit,
        callbackErase: () -> Unit
    ) {
        val user = Firebase.auth.currentUser ?: return

        if (newPassword != newPasswordRepeated) {
            callbackMessage(R.string.passwords_differ)
        } else if (user.email != null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)
            callbackErase()

            user.reauthenticate(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        user.updatePassword(newPassword).addOnCompleteListener {
                            callbackMessage(R.string.password_changed)
                        }
                    } else {
                        callbackMessage(R.string.auth_failed_try_again)
                    }
                }
        }
    }
}
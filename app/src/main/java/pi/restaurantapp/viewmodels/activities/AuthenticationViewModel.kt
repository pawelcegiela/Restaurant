package pi.restaurantapp.viewmodels.activities

import androidx.lifecycle.ViewModel
import pi.restaurantapp.logic.activities.AuthenticationLogic
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.data.user.UserDetails


class AuthenticationViewModel : ViewModel() {
    private val logic = AuthenticationLogic()

    fun getUserBasic(callback: (UserBasic?) -> (Unit)) {
        logic.getUserBasic(callback)
    }

    fun getUserDetails(callback: (UserDetails) -> Unit) {
        logic.getUserDetails(callback)
    }

    fun signIn(email: String, password: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        logic.signIn(email, password, onSuccess, onFailure)
    }

    fun sendPasswordResetEmail(email: String, callback: () -> Unit) {
        logic.sendPasswordResetEmail(email, callback)
    }

    fun createCustomer(email: String, password: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        logic.createCustomer(email, password, onSuccess, onFailure)
    }

    fun addUserDataToDatabase(userId: String, basic: UserBasic, details: UserDetails) {
        logic.addUserDataToDatabase(userId, basic, details)
    }
}
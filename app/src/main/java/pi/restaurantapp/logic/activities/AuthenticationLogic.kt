package pi.restaurantapp.logic.activities

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.data.user.UserDetails

class AuthenticationLogic {
    fun getUserBasic(callback: (UserBasic?) -> (Unit)) {
        Firebase.firestore.collection("users-basic").document(Firebase.auth.uid!!).get().addOnSuccessListener { snapshot ->
            callback(snapshot.toObject())
        }
    }

    fun getUserDetails(callback: (UserDetails) -> Unit) {
        Firebase.firestore.collection("users-details").document(Firebase.auth.uid!!).get().addOnSuccessListener { snapshot ->
            callback(snapshot.toObject() ?: return@addOnSuccessListener)
        }
    }

    fun signIn(email: String, password: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        Firebase.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun sendPasswordResetEmail(email: String, callback: () -> Unit) {
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback()
                }
            }
    }

    fun createCustomer(email: String, password: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun addUserDataToDatabase(userId: String, basic: UserBasic, details: UserDetails) {
        Firebase.firestore.collection("users-basic").document(userId).set(basic)
        Firebase.firestore.collection("users-details").document(userId).set(details)
    }
}
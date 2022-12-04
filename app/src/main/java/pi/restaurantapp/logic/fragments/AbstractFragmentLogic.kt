package pi.restaurantapp.logic.fragments;

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.enums.Role

abstract class AbstractFragmentLogic {
    abstract val databasePath: String

    fun getUserRole(callback: (Int) -> Unit) {
        val userId = Firebase.auth.uid ?: return
        Firebase.firestore.collection("users-basic").document(userId).get().addOnSuccessListener { snapshot ->
            callback(snapshot.getLong("role")?.toInt() ?: Role.getPlaceholder())
        }
    }
}

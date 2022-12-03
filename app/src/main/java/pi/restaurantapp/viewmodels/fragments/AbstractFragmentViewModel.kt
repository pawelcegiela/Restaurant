package pi.restaurantapp.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.enums.Role

abstract class AbstractFragmentViewModel : ViewModel() {
    abstract val databasePath: String

    private val _userRole: MutableLiveData<Int> = MutableLiveData(Role.getPlaceholder())
    val userRole: LiveData<Int> = _userRole

    fun getUserRole() {
        val userId = Firebase.auth.uid ?: return
        Firebase.firestore.collection("users-basic").document(userId).get().addOnSuccessListener { snapshot ->
            _userRole.value = snapshot.getLong("role")?.toInt() ?: Role.getPlaceholder()
        }
    }
}
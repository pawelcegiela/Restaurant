package pi.restaurant.management.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.enums.Role

abstract class AbstractModifyItemViewModel : ViewModel() {
    abstract val databasePath: String

    val liveUserRole = MutableLiveData(Role.WORKER.ordinal)
    val liveDataSnapshot = MutableLiveData<DataSnapshot>()
    val liveSaveStatus = MutableLiveData<Boolean>()

    fun getUserRole() {
        val userId = Firebase.auth.uid ?: return
        val databaseRef = Firebase.database.getReference("users").child(userId).child("role")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                liveUserRole.value = dataSnapshot.getValue<Int>() ?: return
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun getDataFromDatabase(itemId: String) {
        val databaseRef = Firebase.database.getReference(databasePath).child(itemId)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                liveDataSnapshot.value = dataSnapshot
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun saveToDatabase(data: AbstractDataObject) {
        val databaseRef = Firebase.database.getReference(databasePath).child(data.id)
        databaseRef.setValue(data)
    }
}
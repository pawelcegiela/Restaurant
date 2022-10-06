package pi.restaurant.management.logic.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.objects.enums.Role
import pi.restaurant.management.objects.SnapshotsPair

abstract class AbstractPreviewItemViewModel : ViewModel() {
    abstract val databasePath: String

    val liveUserRole = MutableLiveData(Role.getPlaceholder())
    val liveDataSnapshot = MutableLiveData(SnapshotsPair())
    val liveReadyToUnlock = MutableLiveData<Boolean>()

    val snapshotsPair = SnapshotsPair()

    fun getDataFromDatabase(itemId: String) {
        getBasicDataFromDatabase(itemId)
        getDetailsDataFromDatabase(itemId)
    }

    private fun getBasicDataFromDatabase(itemId: String) {
        val databaseRef = Firebase.database.getReference(databasePath).child("basic").child(itemId)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                snapshotsPair.basic = dataSnapshot
                if (snapshotsPair.isReady()) {
                    liveDataSnapshot.value = snapshotsPair
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun getDetailsDataFromDatabase(itemId: String) {
        val databaseRef = Firebase.database.getReference(databasePath).child("details").child(itemId)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                snapshotsPair.details = dataSnapshot
                if (snapshotsPair.isReady()) {
                    liveDataSnapshot.value = snapshotsPair
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun getUserRole() {
        val userId = Firebase.auth.uid ?: return
        val databaseRef = Firebase.database.getReference("users").child("basic").child(userId).child("role")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                liveUserRole.value = dataSnapshot.getValue<Int>() ?: return
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
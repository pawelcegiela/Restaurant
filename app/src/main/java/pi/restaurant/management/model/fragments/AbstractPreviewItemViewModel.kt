package pi.restaurant.management.model.fragments

import androidx.lifecycle.LiveData
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
    private val snapshotsPair = SnapshotsPair()
    var itemId: String = ""

    private val _userRole: MutableLiveData<Int> = MutableLiveData(Role.getPlaceholder())
    val userRole: LiveData<Int> = _userRole

    private val _readyToUnlock: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val readyToUnlock: LiveData<Boolean> = _readyToUnlock

    private val _readyToInitialize: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val readyToInitialize: LiveData<Boolean> = _readyToInitialize

    abstract fun getItem(snapshotsPair: SnapshotsPair)

    fun getDataFromDatabase() {
        getBasicDataFromDatabase()
        getDetailsDataFromDatabase()
    }

    private fun getBasicDataFromDatabase() {
        val databaseRef = Firebase.database.getReference(databasePath).child("basic").child(itemId)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                snapshotsPair.basic = dataSnapshot
                if (snapshotsPair.isReady()) {
                    getItem(snapshotsPair)
                    _readyToInitialize.value = true
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun getDetailsDataFromDatabase() {
        val databaseRef = Firebase.database.getReference(databasePath).child("details").child(itemId)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                snapshotsPair.details = dataSnapshot
                if (snapshotsPair.isReady()) {
                    getItem(snapshotsPair)
                    _readyToInitialize.value = true
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
                _userRole.value = dataSnapshot.getValue<Int>() ?: return
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun setReadyToUnlock() {
        _readyToUnlock.value = true
    }
}
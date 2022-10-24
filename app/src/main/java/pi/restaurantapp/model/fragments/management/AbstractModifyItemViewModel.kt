package pi.restaurantapp.model.fragments.management

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
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.enums.Role

abstract class AbstractModifyItemViewModel : ViewModel() {
    abstract val databasePath: String
    private val snapshotsPair = SnapshotsPair()
    var itemId: String = ""

    private val _userRole: MutableLiveData<Int> = MutableLiveData(Role.getPlaceholder())
    val userRole: LiveData<Int> = _userRole

    private val _saveStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val saveStatus: LiveData<Boolean> = _saveStatus

    private val _readyToInitialize: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val readyToInitialize: LiveData<Boolean> = _readyToInitialize

    open fun getItem(snapshotsPair: SnapshotsPair) {}

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

    fun getDataFromDatabase() {
        getBasicDataFromDatabase()
        getDetailsDataFromDatabase()
    }

    private fun getBasicDataFromDatabase() {
        val databaseRef = Firebase.database.getReference(databasePath).child("basic").child(itemId)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                snapshotsPair.basic = dataSnapshot
                checkIfReady()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun getDetailsDataFromDatabase() {
        val databaseRef = Firebase.database.getReference(databasePath).child("details").child(itemId)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                snapshotsPair.details = dataSnapshot
                checkIfReady()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun checkIfReady() {
        if (snapshotsPair.isReady()) {
            getItem(snapshotsPair)
            getAdditionalData()
        }
    }

    open fun getAdditionalData() {
        setReadyToInitialize()
    }

    open fun saveToDatabase(data: SplitDataObject) {
        val databaseBasicRef = Firebase.database.getReference(databasePath).child("basic").child(data.id)
        databaseBasicRef.setValue(data.basic)

        val databaseDetailsRef = Firebase.database.getReference(databasePath).child("details").child(data.id)
        databaseDetailsRef.setValue(data.details)

        _saveStatus.value = true
    }

    fun removeFromDatabase() {
        removeAdditionalElements()
        for (snapshot in snapshotsPair.basic!!.children) {
            snapshot.ref.removeValue()
        }
        for (snapshot in snapshotsPair.details!!.children) {
            snapshot.ref.removeValue()
        }
    }

    open fun removeAdditionalElements() {}

    fun setReadyToInitialize() {
        _readyToInitialize.value = true
    }

    open fun shouldGetDataFromDatabase() = true

    fun disableItem() {
        val databaseRef = Firebase.database.getReference(databasePath).child("basic").child(itemId).child("disabled")
        databaseRef.setValue(true)
    }
}
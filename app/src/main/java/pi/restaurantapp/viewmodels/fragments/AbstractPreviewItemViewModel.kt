package pi.restaurantapp.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.SnapshotsPair

abstract class AbstractPreviewItemViewModel : AbstractFragmentViewModel() {
    protected val dbRefBasic get() = Firebase.firestore.collection("$databasePath-basic")
    protected val dbRefDetails get() = Firebase.firestore.collection("$databasePath-details")

    private val snapshotsPair = SnapshotsPair()
    var itemId: String = ""

    private val _readyToUnlock: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val readyToUnlock: LiveData<Boolean> = _readyToUnlock

    private val _readyToInitialize: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val readyToInitialize: LiveData<Boolean> = _readyToInitialize

    abstract fun getItem(snapshotsPair: SnapshotsPair)

    open fun getDataFromDatabase() {
        dbRefBasic.document(itemId).get().addOnSuccessListener { snapshot ->
            snapshotsPair.basic = snapshot
            checkIfReady()
        }
        dbRefDetails.document(itemId).get().addOnSuccessListener { snapshot ->
            snapshotsPair.details = snapshot
            checkIfReady()
        }
    }

    private fun checkIfReady() {
        if (snapshotsPair.isReady()) {
            getItem(snapshotsPair)
            _readyToInitialize.value = true
        }
    }

    fun setReadyToUnlock() {
        _readyToUnlock.value = true
    }

    fun setReadyToInitialize() {
        _readyToInitialize.value = true
    }

    open fun shouldGetDataFromDatabase() = true

    abstract fun isDisabled(): Boolean
}
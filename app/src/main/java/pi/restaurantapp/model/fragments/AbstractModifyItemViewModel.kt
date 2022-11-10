package pi.restaurantapp.model.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.SplitDataObject

abstract class AbstractModifyItemViewModel : AbstractFragmentViewModel() {
    protected val dbRefBasic get() = Firebase.firestore.collection("$databasePath-basic")
    protected val dbRefDetails get() = Firebase.firestore.collection("$databasePath-details")

    private val snapshotsPair = SnapshotsPair()
    var itemId: String = ""

    private val _saveStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val saveStatus: LiveData<Boolean> = _saveStatus

    private val _readyToInitialize: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val readyToInitialize: LiveData<Boolean> = _readyToInitialize

    open fun getItem(snapshotsPair: SnapshotsPair) {}

    fun getDataFromDatabase() {
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
            getAdditionalData()
        }
    }

    open fun getAdditionalData() {
        setReadyToInitialize()
    }

    open fun saveToDatabase(data: SplitDataObject) {
        dbRefBasic.document(data.id).set(data.basic)
        dbRefDetails.document(data.id).set(data.details)

        _saveStatus.value = true
    }

    fun removeFromDatabase() {
        removeAdditionalElements()
        dbRefBasic.document(itemId).delete()
        dbRefDetails.document(itemId).delete()
    }

    open fun removeAdditionalElements() {}

    fun setReadyToInitialize() {
        _readyToInitialize.value = true
    }

    open fun shouldGetDataFromDatabase() = true

    fun disableItem() {
        Firebase.firestore.collection("$databasePath-basic").document(itemId).update("disabled", true)
    }
}
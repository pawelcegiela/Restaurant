package pi.restaurantapp.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.logic.fragments.AbstractModifyItemLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.SplitDataObject

abstract class AbstractModifyItemViewModel : AbstractFragmentViewModel() {
    private val _logic: AbstractModifyItemLogic get() = logic as AbstractModifyItemLogic
    var itemId: String = ""

    private val _saveStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val saveStatus: LiveData<Boolean> = _saveStatus

    private val _readyToInitialize: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val readyToInitialize: LiveData<Boolean> = _readyToInitialize

    open fun getItem(snapshotsPair: SnapshotsPair) {}

    open fun createItem() {}

    fun getDataFromDatabase() {
        _logic.getDataFromDatabase(itemId) { snapshotsPair ->
            getItem(snapshotsPair)
            setReadyToInitialize()
        }
    }

    open fun saveToDatabase(data: SplitDataObject) {
        _logic.saveToDatabase(data) { saveStatus ->
            setSaveStatus(saveStatus)
        }
    }

    fun removeFromDatabase() {
        _logic.removeFromDatabase(itemId)
    }

    fun setReadyToInitialize() {
        _readyToInitialize.value = true
    }

    open fun shouldGetDataFromDatabase() = true

    fun disableItem() {
        _logic.disableItem(itemId)
    }

    fun setSaveStatus(status: Boolean) {
        _saveStatus.value = status
    }
}
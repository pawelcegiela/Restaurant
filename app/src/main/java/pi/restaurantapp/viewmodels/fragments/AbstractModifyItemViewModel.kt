package pi.restaurantapp.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.R
import pi.restaurantapp.logic.fragments.AbstractModifyItemLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.NullableSplitDataObject
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.enums.Precondition
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.objects.enums.ToolbarType

abstract class AbstractModifyItemViewModel : AbstractFragmentViewModel() {
    private val _logic: AbstractModifyItemLogic get() = logic as AbstractModifyItemLogic

    var itemId: String = ""
    open var lowestRole = Role.MANAGER.ordinal
    var toolbarType: MutableLiveData<ToolbarType> = MutableLiveData(ToolbarType.HIDDEN)

    abstract val splitDataObject: NullableSplitDataObject

    private val _saveStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val saveStatus: LiveData<Boolean> = _saveStatus

    private val _readyToInitialize: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val readyToInitialize: LiveData<Boolean> = _readyToInitialize

    private val _toastMessage: MutableLiveData<Int> = MutableLiveData<Int>()
    val toastMessage: LiveData<Int> = _toastMessage

    open fun getItem(snapshotsPair: SnapshotsPair) {}

    open fun createItem() {}

    fun getDataFromDatabase() {
        _logic.getDataFromDatabase(itemId) { snapshotsPair ->
            getItem(snapshotsPair)
            setReadyToInitialize()
        }
    }

    open fun saveToDatabase() {
        val precondition = checkSavePreconditions()
        if (precondition != Precondition.OK) {
            _toastMessage.value = precondition.nameRes
            return
        }

        if (splitDataObject.basic != null && splitDataObject.details != null) {
            saveToDatabase2(SplitDataObject(splitDataObject.id, splitDataObject.basic!!, splitDataObject.details!!))
        } else {
            _toastMessage.value = R.string.null_data
        }

    }

    open fun saveToDatabase2(data: SplitDataObject) {
        _logic.saveToDatabase(data) { saveStatus ->
            setSaveStatus(saveStatus)
        }
    }

    open fun checkSavePreconditions(): Precondition {
        return Precondition.OK
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

    fun setToastMessage(toastMessageId: Int) {
        _toastMessage.value = toastMessageId
    }
}
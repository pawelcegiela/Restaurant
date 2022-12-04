package pi.restaurantapp.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.logic.fragments.AbstractPreviewItemLogic
import pi.restaurantapp.objects.SnapshotsPair

abstract class AbstractPreviewItemViewModel : AbstractFragmentViewModel() {
    private val _logic: AbstractPreviewItemLogic get() = logic as AbstractPreviewItemLogic

    var itemId: String = ""

    private val _readyToUnlock: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val readyToUnlock: LiveData<Boolean> = _readyToUnlock

    private val _readyToInitialize: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val readyToInitialize: LiveData<Boolean> = _readyToInitialize

    abstract fun getItem(snapshotsPair: SnapshotsPair)

    open fun getDataFromDatabase() {
        _logic.getDataFromDatabase(itemId) { snapshotsPair ->
            getItem(snapshotsPair)
            setReadyToInitialize()
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
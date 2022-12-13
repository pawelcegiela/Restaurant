package pi.restaurantapp.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.logic.fragments.AbstractPreviewItemLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.objects.enums.ToolbarType

/**
 * Abstract class responsible for presentation logic and binding between data/model and view (ViewModel layer) for AbstractPreviewItemFragment.
 * @see pi.restaurantapp.logic.fragments.AbstractPreviewItemLogic Model layer
 * @see pi.restaurantapp.ui.fragments.AbstractPreviewItemFragment View layer
 */
abstract class AbstractPreviewItemViewModel : AbstractFragmentViewModel() {
    private val _logic: AbstractPreviewItemLogic get() = logic as AbstractPreviewItemLogic

    var itemId: String = ""
    var editable: Boolean = true
    var toolbarType: MutableLiveData<ToolbarType> = MutableLiveData(ToolbarType.HIDDEN)

    private val _readyToUnlock: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val readyToUnlock: LiveData<Boolean> = _readyToUnlock

    private val _readyToInitialize: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val readyToInitialize: LiveData<Boolean> = _readyToInitialize

    fun initializeData(itemId: String) {
        this.itemId = itemId

        logic.getUserRole { role ->
            setUserRole(role)
            if (role != Role.getPlaceholder()) {
                if (shouldGetDataFromDatabase()) {
                    getDataFromDatabase()
                } else {
                    setReadyToInitialize()
                }
            }
        }
    }

    open fun setToolbarType() {
        if (editable && !isDisabled() && Role.isAtLeastManager(userRole.value)) {
            toolbarType.value = ToolbarType.EDIT
        } else {
            toolbarType.value = ToolbarType.BACK
        }
    }

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
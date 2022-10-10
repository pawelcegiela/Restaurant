package pi.restaurant.management.model.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pi.restaurant.management.objects.data.order.Order

class OrdersViewModel : ViewModel() {
    private val _savedOrder = MutableLiveData<Order?>()
    val savedOrder: LiveData<Order?> = _savedOrder

    private val _actionSave = MutableLiveData<Int>()
    val actionSave: LiveData<Int> = _actionSave

    private val _previousStatus: MutableLiveData<Int?> = MutableLiveData()
    val previousStatus: LiveData<Int?> = _previousStatus

    fun setSavedOrder(order: Order?) {
        _savedOrder.value = order
    }

    fun setActionSave(resource: Int) {
        _actionSave.value = resource
    }

    fun setPreviousStatus(status: Int?) {
        _previousStatus.value = status
    }

    fun reset() {
        setSavedOrder(null)
        setPreviousStatus(null)
    }
}
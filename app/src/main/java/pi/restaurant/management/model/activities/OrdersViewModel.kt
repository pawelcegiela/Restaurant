package pi.restaurant.management.model.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pi.restaurant.management.objects.data.delivery.DeliveryBasic
import pi.restaurant.management.objects.data.dish.DishItem
import pi.restaurant.management.objects.data.order.Order

class OrdersViewModel : ViewModel() {
    private val _savedOrder = MutableLiveData<Order?>()
    val savedOrder: LiveData<Order?> = _savedOrder

    private val _actionSave = MutableLiveData<Int>()
    val actionSave: LiveData<Int> = _actionSave

    private val _previousStatus: MutableLiveData<Int?> = MutableLiveData()
    val previousStatus: LiveData<Int?> = _previousStatus

    private val _deliveryOptions: MutableLiveData<DeliveryBasic> = MutableLiveData()
    val deliveryOptions: LiveData<DeliveryBasic> = _deliveryOptions

    private val _editedDish: MutableLiveData<DishItem?> = MutableLiveData()
    val editedDish: LiveData<DishItem?> = _editedDish

    fun setSavedOrder(order: Order?) {
        _savedOrder.value = order
    }

    fun setActionSave(resource: Int) {
        _actionSave.value = resource
    }

    fun setPreviousStatus(status: Int?) {
        _previousStatus.value = status
    }

    fun setDeliveryOptions(deliveryOptions: DeliveryBasic) {
        _deliveryOptions.value = deliveryOptions
    }

    fun setEditedDish(dishItem: DishItem) {
        _editedDish.value = dishItem
    }

    fun resetEditedDish() {
        _editedDish.value = null
    }

    fun reset() {
        setSavedOrder(null)
        setPreviousStatus(null)
    }
}
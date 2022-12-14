package pi.restaurantapp.viewmodels.activities.management

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.objects.data.delivery.DeliveryBasic
import pi.restaurantapp.objects.data.dish.DishItem
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.viewmodels.activities.AbstractActivityViewModel

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for Orders activity.
 * @see pi.restaurantapp.ui.activities.management.OrdersActivity View layer
 */
open class OrdersViewModel : AbstractActivityViewModel() {
    private val _showActive = MutableLiveData(true)
    private val _showDisabled = MutableLiveData(false)

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

    override fun getShowActive(): Boolean {
        return _showActive.value ?: true
    }

    override fun getShowDisabled(): Boolean {
        return _showDisabled.value ?: false
    }

    override fun setShowActive(active: Boolean) {
        _showActive.value = active
    }

    override fun setShowDisabled(disabled: Boolean) {
        _showDisabled.value = disabled
    }

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
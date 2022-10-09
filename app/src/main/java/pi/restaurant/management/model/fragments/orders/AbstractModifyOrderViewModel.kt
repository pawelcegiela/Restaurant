package pi.restaurant.management.model.fragments.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurant.management.objects.data.order.OrderBasic
import pi.restaurant.management.objects.data.order.OrderDetails
import pi.restaurant.management.objects.data.SplitDataObject
import pi.restaurant.management.model.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.objects.data.order.Order
import java.util.*

abstract class AbstractModifyOrderViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "orders"

    private val _item: MutableLiveData<Order> = MutableLiveData()
    val item: LiveData<Order> = _item

    private val _previousStatus: MutableLiveData<Int> = MutableLiveData()
    val previousStatus: LiveData<Int> = _previousStatus

    override fun saveToDatabase(data: SplitDataObject) {
        checkStatusChanges(data.basic as OrderBasic, data.details as OrderDetails)
        super.saveToDatabase(data)
    }

    fun getPreviousItem(): Order {
        return item.value ?: Order(itemId, OrderBasic(), OrderDetails())
    }

    fun setItem(order: Order) {
        _item.value = order
    }

    fun setPreviousStatus(status: Int) {
        _previousStatus.value = status
    }

    private fun checkStatusChanges(basic: OrderBasic, details: OrderDetails) {
        val previousStatus = _previousStatus.value ?: -1
        val newStatus = basic.orderStatus
        val statusChanges = getPreviousItem().details.statusChanges
        if (previousStatus != newStatus) {
            statusChanges[Date().time.toString()] = newStatus
        }
        details.statusChanges = statusChanges
    }

    override fun shouldGetDataFromDatabase(): Boolean {
        if (item.value != null) {
            setReadyToInitialize()
        }
        return item.value == null
    }
}
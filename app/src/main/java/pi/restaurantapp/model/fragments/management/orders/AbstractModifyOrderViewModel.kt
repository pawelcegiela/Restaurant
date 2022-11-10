package pi.restaurantapp.model.fragments.management.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.model.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.delivery.DeliveryBasic
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.order.OrderDetails
import java.util.*

abstract class AbstractModifyOrderViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "orders"

    private val _item: MutableLiveData<Order> = MutableLiveData()
    val item: LiveData<Order> = _item

    private val _previousStatus: MutableLiveData<Int> = MutableLiveData()
    val previousStatus: LiveData<Int> = _previousStatus

    private val _deliveryOptions: MutableLiveData<DeliveryBasic> = MutableLiveData()
    val deliveryOptions: LiveData<DeliveryBasic> = _deliveryOptions

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

    fun setDeliveryOptions(deliveryOptions: DeliveryBasic) {
        _deliveryOptions.value = deliveryOptions
    }

    override fun getAdditionalData() {
        Firebase.firestore.collection("restaurantData-basic").document("delivery").get().addOnSuccessListener { snapshot ->
            _deliveryOptions.value = snapshot.toObject()
            setReadyToInitialize()
        }
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
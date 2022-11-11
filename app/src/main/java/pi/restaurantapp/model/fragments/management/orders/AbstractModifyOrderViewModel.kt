package pi.restaurantapp.model.fragments.management.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.model.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.objects.data.delivery.DeliveryBasic
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.order.OrderDetails

abstract class AbstractModifyOrderViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "orders"

    private val _item: MutableLiveData<Order> = MutableLiveData()
    val item: LiveData<Order> = _item

    private val _previousStatus: MutableLiveData<Int> = MutableLiveData()
    val previousStatus: LiveData<Int> = _previousStatus

    private val _deliveryOptions: MutableLiveData<DeliveryBasic> = MutableLiveData()
    val deliveryOptions: LiveData<DeliveryBasic> = _deliveryOptions

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

    override fun shouldGetDataFromDatabase(): Boolean {
        if (item.value != null) {
            setReadyToInitialize()
        }
        return item.value == null
    }
}
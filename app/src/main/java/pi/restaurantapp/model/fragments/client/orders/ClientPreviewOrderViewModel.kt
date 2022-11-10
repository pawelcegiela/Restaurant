package pi.restaurantapp.model.fragments.client.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.model.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.order.OrderDetails
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.utils.StringFormatUtils
import java.util.*

class ClientPreviewOrderViewModel : AbstractPreviewItemViewModel() {
    override val databasePath = "orders"
    var delivererId = ""

    private val _orderStatus: MutableLiveData<Int> = MutableLiveData<Int>()
    val orderStatus: LiveData<Int> = _orderStatus

    private val _statusChange: MutableLiveData<Pair<String, Int>> = MutableLiveData<Pair<String, Int>>()
    val statusChange: LiveData<Pair<String, Int>> = _statusChange

    private val _delivererName: MutableLiveData<String> = MutableLiveData()
    val delivererName: LiveData<String> = _delivererName

    private val _item: MutableLiveData<Order> = MutableLiveData()
    val item: LiveData<Order> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<OrderBasic>() ?: OrderBasic()
        val details = snapshotsPair.details?.toObject<OrderDetails>() ?: OrderDetails()
        _item.value = Order(itemId, basic, details)
    }

    fun updateOrderStatus(newStatus: Int) {
        val time = Date().time
        Firebase.firestore.runTransaction { transaction ->
            transaction.update(dbRefBasic.document(itemId), "orderStatus", newStatus)
            transaction.update(dbRefDetails.document(itemId), "statusChanges.$time", newStatus)
        }.addOnSuccessListener {
            _orderStatus.value = newStatus
            _statusChange.value = StringFormatUtils.formatDateTime(Date(time)) to newStatus
        }
    }

    fun getDelivererUserName() {
        Firebase.firestore.collection("users-basic").document(delivererId).get().addOnSuccessListener { snapshot ->
            val user = snapshot.toObject<UserBasic>() ?: UserBasic()
            _delivererName.value = user.getFullName()
        }
    }

    override fun isDisabled(): Boolean {
        return item.value?.basic?.disabled == true
    }
}
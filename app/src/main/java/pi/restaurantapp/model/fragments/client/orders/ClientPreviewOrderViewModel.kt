package pi.restaurantapp.model.fragments.client.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.model.fragments.management.AbstractPreviewItemViewModel
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
        val basic = snapshotsPair.basic?.getValue<OrderBasic>() ?: OrderBasic()
        val details = snapshotsPair.details?.getValue<OrderDetails>() ?: OrderDetails()
        _item.value = Order(itemId, basic, details)
    }

    fun updateOrderStatus(newStatus: Int) {
        var databaseRef = Firebase.database.getReference(databasePath).child("basic").child(itemId).child("orderStatus")
        databaseRef.setValue(newStatus)
        _orderStatus.value = newStatus

        val time = Date().time.toString()
        databaseRef = Firebase.database.getReference(databasePath).child("details").child(itemId).child("statusChanges").child(time)
        databaseRef.setValue(newStatus)

        _statusChange.value = StringFormatUtils.formatDateTime(Date(time.toLong())) to newStatus
    }

    fun getDelivererUserName() {
        val databaseRef = Firebase.database.getReference("users").child("basic").child(delivererId)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue<UserBasic>() ?: UserBasic()
                _delivererName.value = user.getFullName()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun isDisabled(): Boolean {
        return item.value?.basic?.disabled == true
    }
}
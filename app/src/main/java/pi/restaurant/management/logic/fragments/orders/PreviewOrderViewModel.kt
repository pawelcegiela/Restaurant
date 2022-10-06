package pi.restaurant.management.logic.fragments.orders

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.objects.data.user.UserBasic
import pi.restaurant.management.logic.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.utils.StringFormatUtils
import java.util.*
import kotlin.collections.HashMap

class PreviewOrderViewModel : AbstractPreviewItemViewModel() {
    override val databasePath = "orders"

    val liveOrderStatus = MutableLiveData<Int>()
    val liveStatusChange = MutableLiveData<Pair<String, Int>>()
    val livePossibleDeliverers = MutableLiveData<MutableList<UserBasic>>()
    var delivererId = ""

    fun updateOrderStatus(newStatus: Int, itemId: String) {
        var databaseRef = Firebase.database.getReference(databasePath).child("basic").child(itemId).child("orderStatus")
        databaseRef.setValue(newStatus)
        liveOrderStatus.value = newStatus

        val time = Date().time.toString()
        databaseRef = Firebase.database.getReference(databasePath).child("details")
            .child(itemId).child("statusChanges").child(time)
        databaseRef.setValue(newStatus)

        liveStatusChange.value = StringFormatUtils.formatDateTime(Date(time.toLong())) to newStatus
    }

    fun updateDeliverer(itemId: String, newDelivererId: String) {
        var databaseRef = Firebase.database.getReference(databasePath).child("details").child(itemId).child("delivererId")
        databaseRef.setValue(newDelivererId)

        databaseRef = Firebase.database.getReference("users").child("details").child(newDelivererId).child("ordersToDeliver").child(itemId)
        databaseRef.setValue(true)

        if (this.delivererId.isNotEmpty() && this.delivererId != newDelivererId) {
            databaseRef = Firebase.database.getReference("users").child("details").child(this.delivererId).child("ordersToDeliver").child(itemId)
            databaseRef.setValue(null)
        }
        this.delivererId = newDelivererId
    }

    fun getAllPossibleDeliverers() {
        val databaseRef = Firebase.database.getReference("users").child("basic")
        val query = databaseRef.orderByChild("delivery").equalTo(true)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val possibleDeliverersMap = dataSnapshot.getValue<HashMap<String, UserBasic>>() ?: HashMap()
                livePossibleDeliverers.value = possibleDeliverersMap.map { it.value }.toMutableList()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
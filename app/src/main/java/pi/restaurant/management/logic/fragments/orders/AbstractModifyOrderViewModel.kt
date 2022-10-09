package pi.restaurant.management.logic.fragments.orders

import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.objects.data.order.OrderBasic
import pi.restaurant.management.objects.data.order.OrderDetails
import pi.restaurant.management.objects.data.SplitDataObject
import pi.restaurant.management.logic.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.objects.data.order.Order
import java.util.*
import kotlin.collections.HashMap

abstract class AbstractModifyOrderViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "orders"
    var order: Order? = null

    override fun saveToDatabase(data: SplitDataObject) {
        checkStatusChanges(data.basic as OrderBasic, data.details as OrderDetails)
        super.saveToDatabase(data)
    }

    private fun checkStatusChanges(basic: OrderBasic, details: OrderDetails) {
        val previousStatus = liveDataSnapshot.value?.basic?.getValue<OrderBasic>()?.orderStatus ?: -1
        val newStatus = basic.orderStatus
        val statusChanges = liveDataSnapshot.value?.details?.getValue<OrderDetails>()?.statusChanges ?: HashMap()
        if (previousStatus != newStatus) {
            statusChanges[Date().time.toString()] = newStatus
        }
        details.statusChanges = statusChanges
    }
}
package pi.restaurant.management.fragments.orders

import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.data.OrderBasic
import pi.restaurant.management.data.OrderDetails
import pi.restaurant.management.data.SplitDataObject
import pi.restaurant.management.fragments.AbstractModifyItemViewModel
import java.util.*
import kotlin.collections.HashMap

abstract class AbstractModifyOrderViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "orders"

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
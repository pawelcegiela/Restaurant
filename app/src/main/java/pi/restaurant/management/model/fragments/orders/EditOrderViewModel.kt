package pi.restaurant.management.model.fragments.orders

import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.objects.SnapshotsPair
import pi.restaurant.management.objects.data.order.Order
import pi.restaurant.management.objects.data.order.OrderBasic
import pi.restaurant.management.objects.data.order.OrderDetails

class EditOrderViewModel : AbstractModifyOrderViewModel() {
    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.getValue<OrderBasic>() ?: OrderBasic()
        val details = snapshotsPair.details?.getValue<OrderDetails>() ?: OrderDetails()
        setItem(Order(itemId, basic, details))
    }

}
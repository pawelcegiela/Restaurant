package pi.restaurantapp.model.fragments.client.neworder

import com.google.firebase.database.ktx.getValue
import pi.restaurantapp.model.fragments.management.orders.AbstractModifyOrderViewModel
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.order.OrderDetails

class ClientFinalizeOrderViewModel : AbstractModifyOrderViewModel() {
    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.getValue<OrderBasic>() ?: OrderBasic()
        val details = snapshotsPair.details?.getValue<OrderDetails>() ?: OrderDetails()
        setItem(Order(itemId, basic, details))
    }

}
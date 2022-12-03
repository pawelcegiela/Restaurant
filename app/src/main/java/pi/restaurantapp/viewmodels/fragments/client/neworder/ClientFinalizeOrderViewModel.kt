package pi.restaurantapp.viewmodels.fragments.client.neworder

import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.viewmodels.fragments.management.orders.AbstractModifyOrderViewModel
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.order.OrderDetails
import pi.restaurantapp.objects.enums.OrderType

class ClientFinalizeOrderViewModel : AbstractModifyOrderViewModel() {
    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<OrderBasic>() ?: OrderBasic()
        val details = snapshotsPair.details?.toObject<OrderDetails>() ?: OrderDetails()
        setItem(Order(itemId, basic, details))

        details.orderType = OrderType.CLIENT_APP.ordinal
    }
}
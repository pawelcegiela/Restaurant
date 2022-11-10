package pi.restaurantapp.model.fragments.management.orders

import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.order.OrderDetails

class EditOrderViewModel : AbstractModifyOrderViewModel() {

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<OrderBasic>() ?: OrderBasic()
        val details = snapshotsPair.details?.toObject<OrderDetails>() ?: OrderDetails()
        setItem(Order(itemId, basic, details))
    }

}
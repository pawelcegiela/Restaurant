package pi.restaurantapp.viewmodels.fragments.management.orders

import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.management.orders.EditOrderLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.order.OrderDetails
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.objects.enums.ToolbarType

class EditOrderViewModel : AbstractModifyOrderViewModel() {
    override val logic = EditOrderLogic()

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<OrderBasic>() ?: OrderBasic()
        val details = snapshotsPair.details?.toObject<OrderDetails>() ?: OrderDetails()
        setItem(Order(itemId, basic, details))

        setToolbarType()
    }

    override fun setToolbarType() {
        toolbarType.value = if (Role.isAtLeastExecutive(userRole.value)) ToolbarType.SAVE_REMOVE else ToolbarType.SAVE
    }
}
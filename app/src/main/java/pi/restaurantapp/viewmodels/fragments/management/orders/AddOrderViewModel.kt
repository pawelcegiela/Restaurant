package pi.restaurantapp.viewmodels.fragments.management.orders

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.logic.fragments.management.orders.AddOrderLogic
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.order.OrderDetails

class AddOrderViewModel : AbstractModifyOrderViewModel() {
    override val logic = AddOrderLogic()

    override fun createItem() {
        itemId = StringFormatUtils.formatId()
        setItem(Order(itemId, OrderBasic(itemId, Firebase.auth.uid!!), OrderDetails(itemId)))
        setReadyToInitialize()
    }
}
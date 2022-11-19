package pi.restaurantapp.model.fragments.management.orders

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.order.OrderDetails
import pi.restaurantapp.utils.StringFormatUtils

class AddOrderViewModel : AbstractModifyOrderViewModel() {
    override fun createItem() {
        itemId = StringFormatUtils.formatId()
        setItem(Order(itemId, OrderBasic(itemId, Firebase.auth.uid!!), OrderDetails(itemId)))
        setReadyToInitialize()
    }
}
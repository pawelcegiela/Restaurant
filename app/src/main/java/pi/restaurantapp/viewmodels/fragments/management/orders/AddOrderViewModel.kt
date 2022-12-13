package pi.restaurantapp.viewmodels.fragments.management.orders

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.logic.fragments.management.orders.AddOrderLogic
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.order.OrderDetails
import pi.restaurantapp.objects.enums.ToolbarType

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for AddOrderFragment.
 * @see pi.restaurantapp.logic.fragments.management.orders.AddOrderLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.orders.AddOrderFragment View layer
 */
class AddOrderViewModel : AbstractModifyOrderViewModel() {
    override val logic = AddOrderLogic()

    override fun createItem() {
        itemId = StringFormatUtils.formatId()
        setItem(Order(itemId, OrderBasic(itemId, Firebase.auth.uid!!), OrderDetails(itemId)))
        setReadyToUnlock()

        setToolbarType()
    }

    override fun setToolbarType() {
        toolbarType.value = ToolbarType.SAVE
    }
}
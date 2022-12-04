package pi.restaurantapp.viewmodels.fragments.client.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.client.orders.ClientPreviewOrderLogic
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.dish.DishItem
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.order.OrderDetails
import pi.restaurantapp.objects.enums.OrderStatus
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel
import java.util.*

open class ClientPreviewOrderViewModel : AbstractPreviewItemViewModel() {
    override val logic = ClientPreviewOrderLogic()

    var delivererId = ""

    private val _orderStatus: MutableLiveData<Int> = MutableLiveData<Int>()
    val orderStatus: LiveData<Int> = _orderStatus

    private val _statusChange: MutableLiveData<Pair<String, Int>> = MutableLiveData<Pair<String, Int>>()
    val statusChange: LiveData<Pair<String, Int>> = _statusChange

    private val _delivererName: MutableLiveData<String> = MutableLiveData()
    val delivererName: LiveData<String> = _delivererName

    private val _item: MutableLiveData<Order> = MutableLiveData()
    val item: LiveData<Order> = _item

    val dishesList = ArrayList<DishItem>()
    val statusChanges = ArrayList<Pair<String, Int>>()

    override fun getItem(snapshotsPair: SnapshotsPair) {
        editable = false

        val basic = snapshotsPair.basic?.toObject<OrderBasic>() ?: OrderBasic()
        val details = snapshotsPair.details?.toObject<OrderDetails>() ?: OrderDetails()
        _item.value = Order(itemId, basic, details)

        dishesList.addAll(details.dishes.toList().map { it.second })
        if (details.statusChanges.isNotEmpty()) {
            val comparator = Comparator { obj1: Pair<Long, Int>, obj2: Pair<Long, Int> -> (obj1.first - obj2.first).toInt() }
            val statusChangesUnsorted = details.statusChanges.map { it.key.toLong() to it.value }
            statusChanges.addAll(statusChangesUnsorted.sortedWith(comparator).map { StringFormatUtils.formatDateTime(Date(it.first)) to it.second })
        }
        delivererId = details.delivererId
        if (delivererId.isNotEmpty()) {
            getDelivererUserName()
        }
        setReadyToUnlock()
    }

    fun cancelOrder() {
        logic.cancelOrder(itemId) { time ->
            _orderStatus.value = OrderStatus.CLOSED_WITHOUT_REALIZATION.ordinal
            _statusChange.value = StringFormatUtils.formatDateTime(Date(time)) to OrderStatus.CLOSED_WITHOUT_REALIZATION.ordinal
        }
    }

    private fun getDelivererUserName() {
        logic.getDelivererUserName(delivererId) { name ->
            _delivererName.value = name
        }
    }

    override fun isDisabled(): Boolean {
        return item.value?.basic?.disabled == true
    }

}
package pi.restaurantapp.viewmodels.fragments.management.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.management.orders.PreviewOrderLogic
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.dish.DishItem
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.order.OrderDetails
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel
import java.util.*

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for PreviewOrderFragment.
 * @see pi.restaurantapp.logic.fragments.management.orders.PreviewOrderLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.orders.PreviewOrderFragment View layer
 */
class PreviewOrderViewModel : AbstractPreviewItemViewModel() {
    override val logic = PreviewOrderLogic()

    var delivererId = ""

    private val _orderStatus: MutableLiveData<Int> = MutableLiveData<Int>()
    val orderStatus: LiveData<Int> = _orderStatus

    private val _statusChange: MutableLiveData<Pair<String, Int>> = MutableLiveData<Pair<String, Int>>()
    val statusChange: LiveData<Pair<String, Int>> = _statusChange

    private val _possibleDeliverers: MutableLiveData<MutableList<UserBasic>> = MutableLiveData<MutableList<UserBasic>>()
    val possibleDeliverers: LiveData<MutableList<UserBasic>> = _possibleDeliverers

    private val _userName: MutableLiveData<String> = MutableLiveData()
    val userName: LiveData<String> = _userName

    private val _item: MutableLiveData<Order> = MutableLiveData()
    val item: LiveData<Order> = _item

    val dishesList = ArrayList<DishItem>()
    val statusChanges = ArrayList<Pair<String, Int>>()

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<OrderBasic>() ?: OrderBasic()
        val details = snapshotsPair.details?.toObject<OrderDetails>() ?: OrderDetails()
        _item.value = Order(itemId, basic, details)

        delivererId = details.delivererId
        dishesList.addAll(details.dishes.toList().map { it.second })
        if (details.statusChanges.isNotEmpty()) {
            val comparator = Comparator { obj1: Pair<Long, Int>, obj2: Pair<Long, Int> -> (obj1.first - obj2.first).toInt() }
            val statusChangesUnsorted = details.statusChanges.map { it.key.toLong() to it.value }
            statusChanges.addAll(statusChangesUnsorted.sortedWith(comparator).map { StringFormatUtils.formatDateTime(Date(it.first)) to it.second })
        }

        logic.getAllPossibleDeliverers { possibleDeliverers ->
            _possibleDeliverers.value = possibleDeliverers
        }
        logic.getUserName(basic.userId) { userName ->
            _userName.value = userName
        }
    }

    fun updateOrderStatus(closeWithoutRealization: Boolean) {
        logic.updateOrderStatus(item.value ?: return, closeWithoutRealization) { newStatus, newChange ->
            _orderStatus.value = newStatus
            _statusChange.value = newChange
        }
    }

    fun updateDeliverer(newDelivererId: String) {
        logic.updateDeliverer(itemId, delivererId, newDelivererId) {
            this.delivererId = newDelivererId
        }
    }

    override fun isDisabled(): Boolean {
        return item.value?.basic?.disabled == true
    }
}
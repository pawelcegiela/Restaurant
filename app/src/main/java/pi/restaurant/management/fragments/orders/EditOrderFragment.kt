package pi.restaurant.management.fragments.orders

import androidx.fragment.app.viewModels
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.Order
import pi.restaurant.management.data.OrderBasic
import pi.restaurant.management.data.OrderDetails
import pi.restaurant.management.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.utils.SnapshotsPair
import pi.restaurant.management.utils.StringFormatUtils


class EditOrderFragment : AbstractModifyOrderFragment() {

    override val nextActionId = R.id.actionEditOrderToOrders
    override val saveMessageId = R.string.order_modified
    override val removeMessageId = R.string.order_removed
    override val addDishAction = R.id.actionEditOrderToDishes
    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : EditOrderViewModel by viewModels()

    override fun initializeUI() {
        super.initializeUI()

        itemId = arguments?.getString("id").toString()
        setNavigationCardsSaveRemove()
    }

    private fun getItem(snapshotsPair: SnapshotsPair) : Order {
        val basic = snapshotsPair.basic?.getValue<OrderBasic>() ?: OrderBasic()
        val details = snapshotsPair.details?.getValue<OrderDetails>() ?: OrderDetails()
        return Order(itemId, basic, details)
    }

    override fun fillInData(snapshotsPair: SnapshotsPair) {
        val data = getItem(snapshotsPair)
        binding.spinnerType.setSelection(data.details.orderType)
        binding.spinnerStatus.setSelection(data.basic.orderStatus)
        binding.editTextCollectionTime.setText(StringFormatUtils.formatTime(data.basic.collectionDate))
        binding.spinnerDelivery.setSelection(data.basic.deliveryType)
        binding.spinnerPlace.setSelection(data.details.orderPlace)
        dishesList = data.details.dishes.toList().map { it.second }.toMutableList()

        addLiveDataListener()
        initializeRecycler()

        finishLoading()
    }
}
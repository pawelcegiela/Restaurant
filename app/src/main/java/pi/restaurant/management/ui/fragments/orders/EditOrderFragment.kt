package pi.restaurant.management.ui.fragments.orders

import androidx.fragment.app.viewModels
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.objects.data.order.Order
import pi.restaurant.management.objects.data.order.OrderBasic
import pi.restaurant.management.objects.data.order.OrderDetails
import pi.restaurant.management.logic.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.logic.fragments.orders.EditOrderViewModel
import pi.restaurant.management.objects.SnapshotsPair
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
        _viewModel.order = Order(itemId, basic, details)
        return _viewModel.order!!
    }

    override fun fillInData(snapshotsPair: SnapshotsPair) {
        val data = getItem(snapshotsPair)
        binding.spinnerType.setSelection(data.details.orderType)
        binding.spinnerStatus.setSelection(data.basic.orderStatus)
        binding.editTextCollectionTime.setText(StringFormatUtils.formatTime(data.basic.collectionDate))
        binding.spinnerDelivery.setSelection(data.basic.deliveryType)
        binding.spinnerPlace.setSelection(data.details.orderPlace)
        binding.address.editTextCity.setText(data.details.address?.city)
        binding.address.editTextPostalCode.setText(data.details.address?.postalCode)
        binding.address.editTextStreet.setText(data.details.address?.street)
        binding.address.editTextHouseNumber.setText(data.details.address?.houseNumber)
        binding.address.editTextFlatNumber.setText(data.details.address?.flatNumber)
        dishesList = data.details.dishes.toList().map { it.second }.toMutableList()

        addLiveDataListener()
        initializeRecycler()

        finishLoading()
    }
}
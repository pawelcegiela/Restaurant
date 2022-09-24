package pi.restaurant.management.fragments.orders

import android.view.View
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.Order
import pi.restaurant.management.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.fragments.workers.EditWorkerViewModel
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
        removeButton.visibility = View.VISIBLE

        setRemoveButtonListener()
    }

    override fun fillInData(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<Order>() ?: return
        binding.spinnerType.setSelection(data.orderType)
        binding.spinnerStatus.setSelection(data.orderStatus)
        binding.editTextCollectionTime.setText(StringFormatUtils.formatTime(data.collectionDate))
        binding.spinnerDelivery.setSelection(data.deliveryType)
        binding.spinnerPlace.setSelection(data.orderPlace)
        dishesList = data.dishes.toList().map { it.second }.toMutableList()

        addLiveDataListener()
        initializeRecycler()

        finishLoading()
    }
}
package pi.restaurant.management.ui.fragments.orders

import androidx.fragment.app.viewModels
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.objects.data.order.Order
import pi.restaurant.management.objects.data.order.OrderBasic
import pi.restaurant.management.objects.data.order.OrderDetails
import pi.restaurant.management.model.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.model.fragments.orders.EditOrderViewModel
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
        activityViewModel.setActionSave(R.id.actionCustomizeDishToEditOrder)
        itemId = arguments?.getString("id") ?: ""
        if (activityViewModel.savedOrder.value != null) {
            itemId = activityViewModel.savedOrder.value!!.id
        }
        setNavigationCardsSaveRemove()
    }
}
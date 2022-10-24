package pi.restaurantapp.ui.fragments.management.orders

import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.model.fragments.management.AbstractModifyItemViewModel
import pi.restaurantapp.model.fragments.management.orders.EditOrderViewModel


class EditOrderFragment : AbstractModifyOrderFragment() {

    override val nextActionId = R.id.actionEditOrderToOrders
    override val saveMessageId = R.string.order_modified
    override val removeMessageId = R.string.order_removed
    override val addDishAction = R.id.actionEditOrderToDishes
    override val editDishActionId = R.id.actionEditOrderToCustomizeDish
    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : EditOrderViewModel by viewModels()

    override fun initializeUI() {
        super.initializeUI()
        activityViewModel.setActionSave(R.id.actionCustomizeDishToEditOrder)
        itemId = arguments?.getString("id") ?: ""
        if (activityViewModel.savedOrder.value != null) {
            itemId = activityViewModel.savedOrder.value!!.id
        }
    }
}
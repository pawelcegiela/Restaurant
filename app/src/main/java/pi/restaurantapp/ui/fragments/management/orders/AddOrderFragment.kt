package pi.restaurantapp.ui.fragments.management.orders

import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.model.fragments.management.AbstractModifyItemViewModel
import pi.restaurantapp.model.fragments.management.orders.AddOrderViewModel

class AddOrderFragment : AbstractModifyOrderFragment() {

    override val nextActionId = R.id.actionAddOrderToOrders
    override val saveMessageId = R.string.order_added
    override val removeMessageId = 0 // Unused
    override val addDishAction = R.id.actionAddOrderToDishes
    override val editDishActionId = R.id.actionAddOrderToCustomizeDish
    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : AddOrderViewModel by viewModels()

    override fun initializeUI() {
        super.initializeUI()
        activityViewModel.setActionSave(R.id.actionCustomizeDishToAddOrder)
        initializeRecycler()
        setNavigationCardsSave()
        finishLoading()
    }
}
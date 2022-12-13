package pi.restaurantapp.ui.fragments.management.orders

import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.orders.AddOrderViewModel

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for AddOrderFragment.
 * @see pi.restaurantapp.viewmodels.fragments.management.orders.AddOrderViewModel ViewModel layer
 * @see pi.restaurantapp.logic.fragments.management.orders.AddOrderLogic Model layer
 */
class AddOrderFragment : AbstractModifyOrderFragment() {

    override val nextActionId = R.id.actionAddOrderToOrders
    override val saveMessageId = R.string.order_added
    override val removeMessageId = 0 // Unused
    override val addDishAction = R.id.actionAddOrderToDishes
    override val editDishActionId = R.id.actionAddOrderToCustomizeDish
    override val viewModel: AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel: AddOrderViewModel by viewModels()

    override fun initializeUI() {
        super.initializeUI()
        activityViewModel.setActionSave(R.id.actionCustomizeDishToAddOrder)
    }
}
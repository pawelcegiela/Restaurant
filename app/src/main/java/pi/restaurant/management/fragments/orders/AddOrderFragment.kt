package pi.restaurant.management.fragments.orders

import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.fragments.workers.EditWorkerViewModel

class AddOrderFragment : AbstractModifyOrderFragment() {

    override val nextActionId = R.id.actionAddOrderToOrders
    override val saveMessageId = R.string.order_added
    override val removeMessageId = 0 // Unused
    override val addDishAction = R.id.actionAddOrderToDishes
    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : AddOrderViewModel by viewModels()

    override fun initializeUI() {
        super.initializeUI()
        initializeRecycler()
        addLiveDataListener()
        finishLoading()
    }
}
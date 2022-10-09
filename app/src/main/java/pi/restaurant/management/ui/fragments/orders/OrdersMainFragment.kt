package pi.restaurant.management.ui.fragments.orders

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.model.activities.OrdersViewModel
import pi.restaurant.management.ui.adapters.OrdersRecyclerAdapter
import pi.restaurant.management.objects.data.order.OrderBasic
import pi.restaurant.management.ui.fragments.AbstractItemListFragment
import pi.restaurant.management.model.fragments.AbstractItemListViewModel
import pi.restaurant.management.model.fragments.orders.OrdersMainViewModel

class OrdersMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionOrdersToAddOrder
    override val editActionId = R.id.actionOrdersToEditOrder
    override val viewModel : AbstractItemListViewModel get() = _viewModel
    private val _viewModel : OrdersMainViewModel by viewModels()
    private val activityViewModel : OrdersViewModel by activityViewModels()

    override fun initializeUI() {
        super.initializeUI()
        activityViewModel.setSavedOrder(null)
        binding.recyclerView.adapter =
            OrdersRecyclerAdapter(viewModel.dataList.value as MutableList<OrderBasic>, this@OrdersMainFragment)
        binding.searchView.visibility = View.GONE
    }
}
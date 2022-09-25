package pi.restaurant.management.fragments.orders

import android.view.View
import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.adapters.OrdersRecyclerAdapter
import pi.restaurant.management.data.Order
import pi.restaurant.management.data.OrderBasic
import pi.restaurant.management.fragments.AbstractItemListFragment
import pi.restaurant.management.fragments.AbstractItemListViewModel

class OrdersMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionOrdersToAddOrder
    override val editActionId = R.id.actionOrdersToEditOrder
    override val viewModel : AbstractItemListViewModel get() = _viewModel
    private val _viewModel : OrdersMainViewModel by viewModels()

    override fun initializeUI() {
        super.initializeUI()
        binding.recyclerView.adapter =
            OrdersRecyclerAdapter(viewModel.liveDataList.value as MutableList<OrderBasic>, this@OrdersMainFragment)
        binding.searchView.visibility = View.GONE
    }
}
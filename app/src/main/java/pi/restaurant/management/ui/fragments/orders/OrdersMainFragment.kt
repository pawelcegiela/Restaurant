package pi.restaurant.management.ui.fragments.orders

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import pi.restaurant.management.R
import pi.restaurant.management.model.activities.OrdersViewModel
import pi.restaurant.management.model.fragments.AbstractItemListViewModel
import pi.restaurant.management.model.fragments.orders.OrdersMainViewModel
import pi.restaurant.management.objects.enums.OrdersTab
import pi.restaurant.management.ui.adapters.PagerAdapter
import pi.restaurant.management.ui.fragments.AbstractItemListFragment

class OrdersMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionOrdersToAddOrder
    override val editActionId = R.id.actionOrdersToEditOrder
    override val viewModel: AbstractItemListViewModel get() = _viewModel
    private val _viewModel: OrdersMainViewModel by viewModels()
    private val activityViewModel: OrdersViewModel by activityViewModels()

    override fun initializeUI() {
        super.initializeUI()
        activityViewModel.reset()
    }

    override fun addViewPagerAdapters() {
        binding.tabLayout.visibility = View.VISIBLE
        val list = OrdersTab.values()
        val names = OrdersTab.getArrayOfStrings(requireContext())
        binding.pager.adapter = PagerAdapter(
            requireActivity().supportFragmentManager,
            lifecycle,
            list,
            requireActivity(),
            this,
            viewModel.dataList.value,
            binding.fabFilter
        )

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = names[position]
        }.attach()
    }
}
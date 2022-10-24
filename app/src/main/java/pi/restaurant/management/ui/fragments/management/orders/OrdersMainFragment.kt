package pi.restaurant.management.ui.fragments.management.orders

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import pi.restaurant.management.R
import pi.restaurant.management.model.activities.management.OrdersViewModel
import pi.restaurant.management.model.fragments.management.AbstractItemListViewModel
import pi.restaurant.management.model.fragments.management.orders.OrdersMainViewModel
import pi.restaurant.management.objects.enums.OrdersTab
import pi.restaurant.management.ui.adapters.PagerAdapter
import pi.restaurant.management.ui.fragments.management.AbstractItemListFragment

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
        val list = OrdersTab.values()
        val names = OrdersTab.getArrayOfStrings(requireContext())
        binding.pager.adapter = PagerAdapter(
            requireActivity().supportFragmentManager,
            lifecycle,
            list,
            requireActivity(),
            this,
            viewModel.dataList.value,
            binding.fabFilter,
            searchView
        )

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = names[position]
        }.attach()
    }
}
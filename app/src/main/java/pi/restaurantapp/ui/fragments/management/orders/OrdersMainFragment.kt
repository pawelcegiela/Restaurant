package pi.restaurantapp.ui.fragments.management.orders

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import pi.restaurantapp.R
import pi.restaurantapp.viewmodels.activities.management.OrdersViewModel
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel
import pi.restaurantapp.viewmodels.fragments.management.orders.OrdersMainViewModel
import pi.restaurantapp.objects.enums.OrdersTab
import pi.restaurantapp.ui.adapters.PagerAdapter
import pi.restaurantapp.ui.fragments.AbstractItemListFragment

class OrdersMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionOrdersToAddOrder
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
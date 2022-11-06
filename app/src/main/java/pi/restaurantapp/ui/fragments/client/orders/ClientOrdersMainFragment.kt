package pi.restaurantapp.ui.fragments.client.orders

import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import pi.restaurantapp.model.fragments.client.orders.ClientOrdersMainViewModel
import pi.restaurantapp.model.fragments.management.AbstractItemListViewModel
import pi.restaurantapp.objects.enums.ClientOrdersTab
import pi.restaurantapp.ui.adapters.PagerAdapter
import pi.restaurantapp.ui.fragments.AbstractItemListFragment

class ClientOrdersMainFragment : AbstractItemListFragment() {
    override val addActionId = 0
    override val viewModel: AbstractItemListViewModel get() = _viewModel
    private val _viewModel: ClientOrdersMainViewModel by viewModels()

    override fun addViewPagerAdapters() {
        val list = ClientOrdersTab.values()
        val names = ClientOrdersTab.getArrayOfStrings(requireContext())
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
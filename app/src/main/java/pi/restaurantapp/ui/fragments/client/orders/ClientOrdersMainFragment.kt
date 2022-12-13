package pi.restaurantapp.ui.fragments.client.orders

import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import pi.restaurantapp.objects.enums.ClientOrdersTab
import pi.restaurantapp.ui.adapters.PagerAdapter
import pi.restaurantapp.ui.fragments.AbstractItemListFragment
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel
import pi.restaurantapp.viewmodels.fragments.client.orders.ClientOrdersMainViewModel

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for ClientOrdersMainFragment.
 * @see pi.restaurantapp.viewmodels.fragments.client.orders.ClientOrdersMainViewModel ViewModel layer
 * @see pi.restaurantapp.logic.fragments.client.orders.ClientOrdersMainLogic Model layer
 */
class ClientOrdersMainFragment : AbstractItemListFragment() {
    override val addActionId = 0
    override val viewModel: AbstractItemListViewModel get() = _viewModel
    private val _viewModel: ClientOrdersMainViewModel by viewModels()

    override fun addViewPagerAdapters() {
        binding.fabFilter.visibility = View.GONE

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
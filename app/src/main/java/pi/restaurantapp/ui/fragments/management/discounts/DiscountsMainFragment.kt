package pi.restaurantapp.ui.fragments.management.discounts

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import pi.restaurantapp.R
import pi.restaurantapp.model.activities.management.DiscountsViewModel
import pi.restaurantapp.model.fragments.AbstractItemListViewModel
import pi.restaurantapp.model.fragments.management.discounts.DiscountsMainViewModel
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.enums.DiscountsTab
import pi.restaurantapp.ui.adapters.PagerAdapter
import pi.restaurantapp.ui.fragments.AbstractItemListFragment

@Suppress("UNCHECKED_CAST")
class DiscountsMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionDiscountsToAddDiscount
    override val viewModel: AbstractItemListViewModel get() = _viewModel
    private val _viewModel: DiscountsMainViewModel by viewModels()

    private val activityViewModel: DiscountsViewModel by activityViewModels()

    override fun addViewPagerAdapters() {
        activityViewModel.setList((viewModel.dataList.value ?: ArrayList()) as ArrayList<DiscountBasic>)

        val list = DiscountsTab.values()
        val names = DiscountsTab.getArrayOfStrings(requireContext())
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
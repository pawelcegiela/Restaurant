package pi.restaurant.management.ui.fragments.discounts

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import pi.restaurant.management.R
import pi.restaurant.management.model.activities.DiscountsViewModel
import pi.restaurant.management.model.fragments.AbstractItemListViewModel
import pi.restaurant.management.model.fragments.discounts.DiscountsMainViewModel
import pi.restaurant.management.objects.data.discount.DiscountBasic
import pi.restaurant.management.objects.enums.DiscountsTab
import pi.restaurant.management.ui.adapters.PagerAdapter
import pi.restaurant.management.ui.fragments.AbstractItemListFragment

@Suppress("UNCHECKED_CAST")
class DiscountsMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionDiscountsToAddDiscount
    override val editActionId = R.id.actionDiscountsToEditDiscount
    override val viewModel: AbstractItemListViewModel get() = _viewModel
    private val _viewModel: DiscountsMainViewModel by viewModels()

    private val activityViewModel: DiscountsViewModel by activityViewModels()

    override fun addViewPagerAdapters() {
        activityViewModel.setList((viewModel.dataList.value ?: ArrayList()) as ArrayList<DiscountBasic>)

        binding.tabLayout.visibility = View.VISIBLE
        val list = DiscountsTab.values()
        val names = DiscountsTab.getArrayOfStrings(requireContext())
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
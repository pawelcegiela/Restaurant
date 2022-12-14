package pi.restaurantapp.ui.fragments.management.discounts

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import pi.restaurantapp.R
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.enums.DiscountsTab
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.ui.adapters.PagerAdapter
import pi.restaurantapp.ui.fragments.AbstractItemListFragment
import pi.restaurantapp.viewmodels.activities.management.DiscountsViewModel
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel
import pi.restaurantapp.viewmodels.fragments.management.discounts.DiscountsMainViewModel

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for DiscountsMainFragment.
 * @see pi.restaurantapp.viewmodels.fragments.management.discounts.DiscountsMainViewModel ViewModel layer
 * @see pi.restaurantapp.logic.fragments.management.discounts.DiscountsMainLogic Model layer
 */
@Suppress("UNCHECKED_CAST")
class DiscountsMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionDiscountsToAddDiscount
    override val viewModel: AbstractItemListViewModel get() = _viewModel
    private val _viewModel: DiscountsMainViewModel by viewModels()

    private val activityViewModel: DiscountsViewModel by activityViewModels()

    override fun addViewPagerAdapters() {
        activityViewModel.setList((viewModel.dataList.value ?: ArrayList()) as ArrayList<DiscountBasic>)
        if (Role.isAtLeastManager(viewModel.userRole.value)) {
            binding.fabReward.visibility = View.VISIBLE
        }
        binding.fabReward.setOnClickListener {
            findNavController().navigate(R.id.actionDiscountsToAddReward)
        }

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
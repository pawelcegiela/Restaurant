package pi.restaurant.management.ui.fragments.management.dishes

import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import pi.restaurant.management.R
import pi.restaurant.management.model.fragments.management.AbstractItemListViewModel
import pi.restaurant.management.model.fragments.management.dishes.DishesMainViewModel
import pi.restaurant.management.objects.enums.DishType
import pi.restaurant.management.objects.enums.DishesTab
import pi.restaurant.management.ui.activities.management.OrdersActivity
import pi.restaurant.management.ui.adapters.PagerAdapter
import pi.restaurant.management.ui.fragments.management.AbstractItemListFragment

class DishesMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionDishesToAddDish
    override val editActionId = R.id.actionDishesToEditDish
    override val viewModel: AbstractItemListViewModel get() = _viewModel
    private val _viewModel: DishesMainViewModel by viewModels()

    override fun initializeUI() {
        super.initializeUI()
        if (activity is OrdersActivity) {
            _viewModel.shouldDisplayFAB = false
        }
    }

    override fun addViewPagerAdapters() {
        val list = DishType.values()
        val names = DishesTab.getArrayOfStrings(requireContext())
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
package pi.restaurantapp.ui.fragments.management.dishes

import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import pi.restaurantapp.R
import pi.restaurantapp.model.fragments.management.AbstractItemListViewModel
import pi.restaurantapp.model.fragments.management.dishes.DishesMainViewModel
import pi.restaurantapp.objects.enums.DishType
import pi.restaurantapp.objects.enums.DishesTab
import pi.restaurantapp.ui.activities.management.OrdersActivity
import pi.restaurantapp.ui.adapters.PagerAdapter
import pi.restaurantapp.ui.fragments.management.AbstractItemListFragment

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
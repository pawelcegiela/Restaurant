package pi.restaurantapp.ui.fragments.management.ingredients

import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import pi.restaurantapp.R
import pi.restaurantapp.model.fragments.AbstractItemListViewModel
import pi.restaurantapp.model.fragments.management.ingredients.IngredientsMainViewModel
import pi.restaurantapp.objects.enums.IngredientsTab
import pi.restaurantapp.ui.adapters.PagerAdapter
import pi.restaurantapp.ui.fragments.AbstractItemListFragment

class IngredientsMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionIngredientsToAddIngredient
    override val viewModel: AbstractItemListViewModel get() = _viewModel
    private val _viewModel: IngredientsMainViewModel by viewModels()

    override fun addViewPagerAdapters() {
        val list = IngredientsTab.values()
        val names = IngredientsTab.getArrayOfStrings(requireContext())
        binding.pager.adapter =
            PagerAdapter(
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
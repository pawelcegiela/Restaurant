package pi.restaurantapp.ui.fragments.management.ingredients

import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import pi.restaurantapp.R
import pi.restaurantapp.objects.enums.IngredientsTab
import pi.restaurantapp.ui.adapters.PagerAdapter
import pi.restaurantapp.ui.fragments.AbstractItemListFragment
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel
import pi.restaurantapp.viewmodels.fragments.management.ingredients.IngredientsMainViewModel

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for IngredientsMainFragment.
 * @see pi.restaurantapp.viewmodels.fragments.management.ingredients.IngredientsMainViewModel ViewModel layer
 * @see pi.restaurantapp.logic.fragments.management.ingredients.IngredientsMainLogic Model layer
 */
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
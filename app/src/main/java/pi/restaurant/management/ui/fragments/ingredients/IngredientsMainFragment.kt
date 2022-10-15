package pi.restaurant.management.ui.fragments.ingredients

import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import pi.restaurant.management.R
import pi.restaurant.management.model.fragments.AbstractItemListViewModel
import pi.restaurant.management.model.fragments.ingredients.IngredientsMainViewModel
import pi.restaurant.management.objects.enums.IngredientsTab
import pi.restaurant.management.ui.adapters.PagerAdapter
import pi.restaurant.management.ui.fragments.AbstractItemListFragment

class IngredientsMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionIngredientsToAddIngredient
    override val editActionId = R.id.actionIngredientsToEditIngredient
    override val viewModel: AbstractItemListViewModel get() = _viewModel
    private val _viewModel: IngredientsMainViewModel by viewModels()

    override fun addViewPagerAdapters() {
        binding.tabLayout.visibility = View.VISIBLE
        val list = IngredientsTab.values()
        val names = IngredientsTab.getArrayOfStrings(requireContext())
        binding.pager.adapter =
            PagerAdapter(requireActivity().supportFragmentManager, lifecycle, list, requireActivity(), this, viewModel.dataList.value)

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = names[position]
        }.attach()
    }
}
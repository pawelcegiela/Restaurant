package pi.restaurant.management.ui.fragments.allergens

import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import pi.restaurant.management.R
import pi.restaurant.management.model.fragments.AbstractItemListViewModel
import pi.restaurant.management.model.fragments.allergens.AllergensMainViewModel
import pi.restaurant.management.ui.adapters.PagerAdapter
import pi.restaurant.management.ui.fragments.AbstractItemListFragment

class AllergensMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionAllergensToAddAllergen
    override val editActionId = R.id.actionAllergensToEditAllergen
    override val viewModel: AbstractItemListViewModel get() = _viewModel
    private val _viewModel: AllergensMainViewModel by viewModels()

    override fun addViewPagerAdapters() {
        val list = arrayOf(0)
        val names = arrayListOf(getString(R.string.all_))
        binding.pager.adapter =
            PagerAdapter(requireActivity().supportFragmentManager, lifecycle, list, requireActivity(), this, viewModel.dataList.value)

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = names[position]
        }.attach()
    }
}
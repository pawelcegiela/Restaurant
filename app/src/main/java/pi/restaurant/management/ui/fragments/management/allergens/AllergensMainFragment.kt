package pi.restaurant.management.ui.fragments.management.allergens

import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import pi.restaurant.management.R
import pi.restaurant.management.model.fragments.management.AbstractItemListViewModel
import pi.restaurant.management.model.fragments.management.allergens.AllergensMainViewModel
import pi.restaurant.management.ui.adapters.PagerAdapter
import pi.restaurant.management.ui.fragments.management.AbstractItemListFragment

class AllergensMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionAllergensToAddAllergen
    override val editActionId = R.id.actionAllergensToEditAllergen
    override val viewModel: AbstractItemListViewModel get() = _viewModel
    private val _viewModel: AllergensMainViewModel by viewModels()

    override fun addViewPagerAdapters() {
        binding.tabLayout.visibility = View.GONE
        val list = arrayOf(0)
        val names = arrayListOf(getString(R.string.all_))
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
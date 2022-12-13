package pi.restaurantapp.ui.fragments.management.allergens

import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import pi.restaurantapp.R
import pi.restaurantapp.ui.adapters.PagerAdapter
import pi.restaurantapp.ui.fragments.AbstractItemListFragment
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel
import pi.restaurantapp.viewmodels.fragments.management.allergens.AllergensMainViewModel

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for AllergensMainFragment.
 * @see pi.restaurantapp.viewmodels.fragments.management.allergens.AllergensMainViewModel ViewModel layer
 * @see pi.restaurantapp.logic.fragments.management.allergens.AllergensMainLogic Model layer
 */
class AllergensMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionAllergensToAddAllergen
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
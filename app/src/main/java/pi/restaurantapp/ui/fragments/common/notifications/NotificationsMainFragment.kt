package pi.restaurantapp.ui.fragments.common.notifications

import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import pi.restaurantapp.R
import pi.restaurantapp.ui.adapters.PagerAdapter
import pi.restaurantapp.ui.fragments.AbstractItemListFragment
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel
import pi.restaurantapp.viewmodels.fragments.common.notifications.NotificationsMainViewModel

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for NotificationsMainFragment.
 * @see pi.restaurantapp.viewmodels.fragments.common.notifications.NotificationsMainViewModel ViewModel layer
 * @see pi.restaurantapp.logic.fragments.common.notifications.NotificationsMainLogic Model layer
 */
class NotificationsMainFragment : AbstractItemListFragment() {
    override val addActionId = 0 // Unused
    override val viewModel: AbstractItemListViewModel get() = _viewModel
    private val _viewModel: NotificationsMainViewModel by viewModels()

    override fun addViewPagerAdapters() {
        binding.tabLayout.visibility = View.GONE
        binding.fabFilter.visibility = View.GONE
        binding.fabAdd.visibility = View.GONE

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
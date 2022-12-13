package pi.restaurantapp.ui.fragments.management.chats

import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import pi.restaurantapp.R
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.ui.adapters.PagerAdapter
import pi.restaurantapp.ui.fragments.AbstractItemListFragment
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel
import pi.restaurantapp.viewmodels.fragments.management.chats.ChatsChooseCustomerViewModel

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for ChatsChooseCustomerFragment.
 * @see pi.restaurantapp.viewmodels.fragments.management.chats.ChatsChooseCustomerViewModel ViewModel layer
 * @see pi.restaurantapp.logic.fragments.management.chats.ChatsChooseCustomerLogic Model layer
 */
class ChatsChooseCustomerFragment : AbstractItemListFragment() {
    override val addActionId = 0
    override val viewModel: AbstractItemListViewModel get() = _viewModel
    private val _viewModel: ChatsChooseCustomerViewModel by viewModels()

    override fun addViewPagerAdapters() {
        binding.tabLayout.visibility = View.GONE
        if (!Role.isAtLeastManager(viewModel.userRole.value)) {
            binding.pager.visibility = View.GONE
            return
        }
        val list = arrayOf(0)
        val names = arrayListOf(getString(R.string.all_))
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
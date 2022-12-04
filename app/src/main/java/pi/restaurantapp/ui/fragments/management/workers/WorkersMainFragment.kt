package pi.restaurantapp.ui.fragments.management.workers

import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import pi.restaurantapp.R
import pi.restaurantapp.objects.enums.WorkersTab
import pi.restaurantapp.ui.adapters.PagerAdapter
import pi.restaurantapp.ui.fragments.AbstractItemListFragment
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel
import pi.restaurantapp.viewmodels.fragments.management.workers.WorkersMainViewModel

class WorkersMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionWorkersToAddWorker
    override val viewModel: AbstractItemListViewModel get() = _viewModel
    private val _viewModel: WorkersMainViewModel by viewModels()

    override fun addViewPagerAdapters() {
        val list = WorkersTab.values()
        val names = WorkersTab.getArrayOfStrings(requireContext())
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
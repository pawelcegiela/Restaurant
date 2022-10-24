package pi.restaurant.management.ui.fragments.management.workers

import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import pi.restaurant.management.R
import pi.restaurant.management.model.fragments.management.AbstractItemListViewModel
import pi.restaurant.management.objects.enums.WorkersTab
import pi.restaurant.management.ui.adapters.PagerAdapter
import pi.restaurant.management.ui.fragments.management.AbstractItemListFragment

class WorkersMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionWorkersToAddWorker
    override val editActionId = R.id.actionWorkersToEditWorker
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
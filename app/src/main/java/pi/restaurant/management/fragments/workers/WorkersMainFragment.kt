package pi.restaurant.management.fragments.workers

import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.adapters.WorkersRecyclerAdapter
import pi.restaurant.management.data.User
import pi.restaurant.management.data.UserBasic
import pi.restaurant.management.fragments.AbstractItemListFragment
import pi.restaurant.management.fragments.AbstractItemListViewModel

class WorkersMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionWorkersToAddWorker
    override val editActionId = R.id.actionWorkersToEditWorker
    override val viewModel : AbstractItemListViewModel get() = _viewModel
    private val _viewModel : WorkersMainViewModel by viewModels()

    override fun initializeUI() {
        super.initializeUI()
        binding.recyclerView.adapter =
            WorkersRecyclerAdapter(viewModel.liveDataList.value as MutableList<UserBasic>, this@WorkersMainFragment)
    }
}
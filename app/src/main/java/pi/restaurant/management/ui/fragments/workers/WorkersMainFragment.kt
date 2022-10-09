package pi.restaurant.management.ui.fragments.workers

import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.ui.adapters.WorkersRecyclerAdapter
import pi.restaurant.management.objects.data.user.UserBasic
import pi.restaurant.management.ui.fragments.AbstractItemListFragment
import pi.restaurant.management.model.fragments.AbstractItemListViewModel

class WorkersMainFragment : AbstractItemListFragment() {
    override val addActionId = R.id.actionWorkersToAddWorker
    override val editActionId = R.id.actionWorkersToEditWorker
    override val viewModel : AbstractItemListViewModel get() = _viewModel
    private val _viewModel : WorkersMainViewModel by viewModels()

    override fun initializeUI() {
        super.initializeUI()
        binding.recyclerView.adapter =
            WorkersRecyclerAdapter(viewModel.dataList.value as MutableList<UserBasic>, this@WorkersMainFragment)
    }
}
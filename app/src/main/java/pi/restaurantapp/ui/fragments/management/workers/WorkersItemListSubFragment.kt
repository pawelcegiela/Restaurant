package pi.restaurantapp.ui.fragments.management.workers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pi.restaurantapp.viewmodels.activities.management.WorkersViewModel
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.objects.enums.WorkersTab
import pi.restaurantapp.ui.adapters.WorkersRecyclerAdapter
import pi.restaurantapp.ui.fragments.ItemListSubFragment

class WorkersItemListSubFragment(
    private var list: MutableList<UserBasic>,
    private val position: Int,
    fabFilter: FloatingActionButton,
    searchView: SearchView
) : ItemListSubFragment(fabFilter, searchView) {
    private val _activityViewModel: WorkersViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        activityViewModel = _activityViewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (position) {
            WorkersTab.ADMINS.ordinal -> list = list.filter { it.role == Role.ADMIN.ordinal }.toMutableList()
            WorkersTab.OWNERS.ordinal -> list = list.filter { it.role == Role.OWNER.ordinal }.toMutableList()
            WorkersTab.EXECUTIVES.ordinal -> list = list.filter { it.role == Role.EXECUTIVE.ordinal }.toMutableList()
            WorkersTab.MANAGERS.ordinal -> list = list.filter { it.role == Role.MANAGER.ordinal }.toMutableList()
            WorkersTab.WORKERS.ordinal -> list = list.filter { it.role == Role.WORKER.ordinal }.toMutableList()
        }

        setAdapter()
    }

    private fun getFilteredList(): MutableList<UserBasic> {
        return if (_activityViewModel.getShowActive() && _activityViewModel.getShowDisabled()) {
            list
        } else if (!_activityViewModel.getShowActive() && !_activityViewModel.getShowDisabled()) {
            ArrayList()
        } else {
            list.filter { it.disabled == _activityViewModel.getShowDisabled() }.toMutableList()
        }
    }

    override fun setAdapter() {
        binding.recyclerView.adapter = WorkersRecyclerAdapter(getFilteredList(), this)
    }
}
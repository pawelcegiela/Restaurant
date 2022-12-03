package pi.restaurantapp.ui.fragments.management.customers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pi.restaurantapp.viewmodels.activities.management.CustomersViewModel
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.ui.adapters.CustomersRecyclerAdapter
import pi.restaurantapp.ui.fragments.ItemListSubFragment


class CustomersItemListSubFragment(
    private var list: MutableList<UserBasic>,
    fabFilter: FloatingActionButton,
    searchView: SearchView
) : ItemListSubFragment(fabFilter, searchView) {
    private val _activityViewModel: CustomersViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        activityViewModel = _activityViewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        binding.recyclerView.adapter = CustomersRecyclerAdapter(getFilteredList(), this)
    }
}
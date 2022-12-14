package pi.restaurantapp.ui.fragments.common.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pi.restaurantapp.objects.data.notification.Notification
import pi.restaurantapp.ui.adapters.NotificationsRecyclerAdapter
import pi.restaurantapp.ui.fragments.ItemListSubFragment
import pi.restaurantapp.viewmodels.activities.common.NotificationsViewModel

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for NotificationsItemListSubFragment.
 */
class NotificationsItemListSubFragment(private val list: MutableList<Notification>, fabFilter: FloatingActionButton, searchView: SearchView) :
    ItemListSubFragment(fabFilter, searchView) {
    private val _activityViewModel: NotificationsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        activityViewModel = _activityViewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
    }

    private fun getFilteredList(): MutableList<Notification> {
        return list
    }

    override fun setAdapter() {
        binding.recyclerView.adapter = NotificationsRecyclerAdapter(getFilteredList(), this)
    }
}
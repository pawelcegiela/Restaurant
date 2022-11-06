package pi.restaurantapp.ui.fragments.client.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pi.restaurantapp.model.activities.management.OrdersViewModel
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.enums.ClientOrdersTab
import pi.restaurantapp.objects.enums.OrderStatus
import pi.restaurantapp.ui.adapters.OrdersRecyclerAdapter
import pi.restaurantapp.ui.fragments.ItemListSubFragment

class ClientOrdersItemListSubFragment(
    private var list: MutableList<OrderBasic>,
    private val position: Int,
    fabFilter: FloatingActionButton,
    private val searchView: SearchView
) : ItemListSubFragment(fabFilter, searchView) {
    private val _activityViewModel: OrdersViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        activityViewModel = _activityViewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (position) {
            ClientOrdersTab.ACTIVE.ordinal -> list = list.filter { it.orderStatus < OrderStatus.FINISHED.ordinal }.toMutableList()
            ClientOrdersTab.FINISHED.ordinal -> list = list.filter { it.orderStatus == OrderStatus.FINISHED.ordinal }.toMutableList()
            ClientOrdersTab.CANCELED.ordinal -> list =
                list.filter { it.orderStatus == OrderStatus.CLOSED_WITHOUT_REALIZATION.ordinal }.toMutableList()
        }

        setAdapter()
        searchView.visibility = View.GONE
    }

    private fun getFilteredList(): MutableList<OrderBasic> {
        return if (_activityViewModel.getShowActive() && _activityViewModel.getShowDisabled()) {
            list
        } else if (!_activityViewModel.getShowActive() && !_activityViewModel.getShowDisabled()) {
            ArrayList()
        } else {
            list.filter { it.disabled == _activityViewModel.getShowDisabled() }.toMutableList()
        }
    }

    override fun setAdapter() {
        binding.recyclerView.adapter = OrdersRecyclerAdapter(getFilteredList(), this)
    }
}
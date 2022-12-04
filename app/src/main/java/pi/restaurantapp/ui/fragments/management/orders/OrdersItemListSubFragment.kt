package pi.restaurantapp.ui.fragments.management.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.enums.OrderStatus
import pi.restaurantapp.objects.enums.OrdersTab
import pi.restaurantapp.ui.adapters.OrdersRecyclerAdapter
import pi.restaurantapp.ui.fragments.ItemListSubFragment
import pi.restaurantapp.viewmodels.activities.management.OrdersViewModel

class OrdersItemListSubFragment(
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
            OrdersTab.ALL_ACTIVE.ordinal -> list = list.filter { it.orderStatus < OrderStatus.FINISHED.ordinal }.toMutableList()
            OrdersTab.NEW.ordinal -> list = list.filter { it.orderStatus == OrderStatus.NEW.ordinal }.toMutableList()
            OrdersTab.ACCEPTED.ordinal -> list = list.filter { it.orderStatus == OrderStatus.ACCEPTED.ordinal }.toMutableList()
            OrdersTab.PREPARING.ordinal -> list = list.filter { it.orderStatus == OrderStatus.PREPARING.ordinal }.toMutableList()
            OrdersTab.READY.ordinal -> list = list.filter { it.orderStatus == OrderStatus.READY.ordinal }.toMutableList()
            OrdersTab.DELIVERY.ordinal -> list = list.filter { it.orderStatus == OrderStatus.DELIVERY.ordinal }.toMutableList()
            OrdersTab.FINISHED.ordinal -> list = list.filter { it.orderStatus == OrderStatus.FINISHED.ordinal }.toMutableList()
            OrdersTab.CLOSED_WITHOUT_REALIZATION.ordinal -> list =
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
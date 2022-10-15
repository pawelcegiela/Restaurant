package pi.restaurant.management.ui.fragments.orders

import android.os.Bundle
import android.view.View
import pi.restaurant.management.objects.data.order.OrderBasic
import pi.restaurant.management.objects.enums.OrderStatus
import pi.restaurant.management.objects.enums.OrdersTab
import pi.restaurant.management.objects.enums.Role
import pi.restaurant.management.objects.enums.WorkersTab
import pi.restaurant.management.ui.adapters.OrdersRecyclerAdapter
import pi.restaurant.management.ui.fragments.ItemListSubFragment

class OrdersItemListSubFragment(private var list: MutableList<OrderBasic>, private val position: Int) : ItemListSubFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (position) {
            OrdersTab.NEW.ordinal -> list = list.filter { it.orderStatus == OrderStatus.NEW.ordinal }.toMutableList()
            OrdersTab.ACCEPTED.ordinal -> list = list.filter { it.orderStatus == OrderStatus.ACCEPTED.ordinal }.toMutableList()
            OrdersTab.PREPARING.ordinal -> list = list.filter { it.orderStatus == OrderStatus.PREPARING.ordinal }.toMutableList()
            OrdersTab.READY.ordinal -> list = list.filter { it.orderStatus == OrderStatus.READY.ordinal }.toMutableList()
            OrdersTab.DELIVERY.ordinal -> list = list.filter { it.orderStatus == OrderStatus.DELIVERY.ordinal }.toMutableList()
            OrdersTab.FINISHED.ordinal -> list = list.filter { it.orderStatus == OrderStatus.FINISHED.ordinal }.toMutableList()
            OrdersTab.CLOSED_WITHOUT_REALIZATION.ordinal -> list = list.filter { it.orderStatus == OrderStatus.CLOSED_WITHOUT_REALIZATION.ordinal }.toMutableList()
        }
        binding.recyclerView.adapter = OrdersRecyclerAdapter(list, this)
        binding.searchView.visibility = View.GONE
    }
}
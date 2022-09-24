package pi.restaurant.management.fragments.orders

import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.adapters.OrdersRecyclerAdapter
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.Order
import pi.restaurant.management.fragments.AbstractItemListFragment

class OrdersMainFragment : AbstractItemListFragment() {
    override val databasePath = "orders"
    override val addActionId = R.id.actionOrdersToAddOrder
    override val editActionId = R.id.actionOrdersToEditOrder

    override fun setData(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, Order>>() ?: return
        val list = data.toList().map { it.second }.toMutableList()
        binding.recyclerView.adapter =
            OrdersRecyclerAdapter(list, this@OrdersMainFragment)
        adapterData = list as MutableList<AbstractDataObject>
        binding.searchView.visibility = View.GONE // No usage so far
    }
}
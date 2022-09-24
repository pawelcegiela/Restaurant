package pi.restaurant.management.fragments.orders

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.data.Order
import pi.restaurant.management.fragments.AbstractItemListViewModel

class OrdersMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "orders"

    override fun setDataList(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, Order>>() ?: return
        liveDataList.value = data.toList().map { it.second }.toMutableList()
    }
}
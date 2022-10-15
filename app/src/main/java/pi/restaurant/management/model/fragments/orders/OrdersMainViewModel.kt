package pi.restaurant.management.model.fragments.orders

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.model.fragments.AbstractItemListViewModel
import pi.restaurant.management.objects.data.order.OrderBasic

class OrdersMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "orders"

    override fun retrieveDataList(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, OrderBasic>>() ?: HashMap()
        setDataList(data.toList().map { it.second }.toMutableList().sortedByDescending { it.id }.toMutableList())
    }
}
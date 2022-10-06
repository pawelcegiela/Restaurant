package pi.restaurant.management.logic.fragments.orders

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.objects.data.order.OrderBasic
import pi.restaurant.management.logic.fragments.AbstractItemListViewModel

class OrdersMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "orders"

    override fun setDataList(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, OrderBasic>>() ?: HashMap()
        liveDataList.value = data.toList().map { it.second }.toMutableList()
    }
}
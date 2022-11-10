package pi.restaurantapp.model.fragments.management.orders

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.model.fragments.AbstractItemListViewModel
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.order.OrderBasic
import java.util.*

class OrdersMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "orders"
    override val dbRef get() = Firebase.firestore.collection("$databasePath-basic").whereGreaterThan("collectionDate", getMonthAgoDate())

    override fun retrieveDataList(snapshot: QuerySnapshot) {
        val dataList = snapshot.map { document -> document.toObject<OrderBasic>() }.toMutableList<AbstractDataObject>()
        setDataList(dataList)
    }

    private fun getMonthAgoDate(): Date {
        val monthInMillis = 1000L * 60 * 60 * 24 * 30
        return Date(Date().time - monthInMillis)
    }
}
package pi.restaurantapp.viewmodels.fragments.client.orders

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.order.OrderBasic

class ClientOrdersMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "orders"
    override val dbRef get() = Firebase.firestore.collection("$databasePath-basic")
        .whereEqualTo("userId", Firebase.auth.uid).orderBy("collectionDate", Query.Direction.DESCENDING)

    override fun retrieveDataList(snapshot: QuerySnapshot) {
        val dataList = snapshot.map { document -> document.toObject<OrderBasic>() }.toMutableList<AbstractDataObject>()
        setDataList(dataList)
    }
}
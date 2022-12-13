package pi.restaurantapp.logic.fragments.client.orders

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.logic.fragments.AbstractItemListLogic
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.order.OrderBasic

/**
 * Class responsible for business logic and communication with database (Model layer) for ClientOrdersMainFragment.
 * @see pi.restaurantapp.ui.fragments.client.orders.ClientOrdersMainFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.client.orders.ClientOrdersMainViewModel ViewModel layer
 */
class ClientOrdersMainLogic : AbstractItemListLogic() {
    override val databasePath = "orders"
    override val dbRef
        get() = Firebase.firestore.collection("$databasePath-basic")
            .whereEqualTo("userId", Firebase.auth.uid).orderBy("collectionDate", Query.Direction.DESCENDING)

    override fun retrieveDataList(snapshot: QuerySnapshot, callback: (MutableList<AbstractDataObject>) -> Unit) {
        val dataList = snapshot.map { document -> document.toObject<OrderBasic>() }.toMutableList<AbstractDataObject>()
        callback(dataList)
    }
}
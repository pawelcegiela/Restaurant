package pi.restaurantapp.model.fragments.management.customers

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.model.fragments.AbstractItemListViewModel
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.enums.Role

class CustomersMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "users"
    override val dbRef get() = Firebase.firestore.collection("$databasePath-basic").whereEqualTo("role", Role.CUSTOMER.ordinal)

    override fun retrieveDataList(snapshot: QuerySnapshot) {
        val dataList = snapshot.map { document -> document.toObject<UserBasic>() }.toMutableList<AbstractDataObject>()
        setDataList(dataList)
    }

}
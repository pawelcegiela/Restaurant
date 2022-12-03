package pi.restaurantapp.viewmodels.fragments.management.workers

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.enums.Role

class WorkersMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "users"
    override val dbRef get() = Firebase.firestore.collection("$databasePath-basic").whereNotEqualTo("role", Role.CUSTOMER.ordinal)

    override fun retrieveDataList(snapshot: QuerySnapshot) {
        val dataList = snapshot.map { document -> document.toObject<UserBasic>() }.toMutableList<AbstractDataObject>()
        setDataList(dataList)
    }

}
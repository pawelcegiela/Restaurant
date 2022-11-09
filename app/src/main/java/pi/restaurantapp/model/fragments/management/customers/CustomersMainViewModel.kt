package pi.restaurantapp.model.fragments.management.customers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurantapp.model.fragments.management.AbstractItemListViewModel
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.enums.Role

//TODO FILTROWANIE - W QUERY
class CustomersMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "users"

    override fun retrieveDataList(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, UserBasic>>() ?: HashMap()
        setDataList(data.toList().map { it.second }.filter { !Role.isWorkerRole(it.role) }.toMutableList())
    }

}
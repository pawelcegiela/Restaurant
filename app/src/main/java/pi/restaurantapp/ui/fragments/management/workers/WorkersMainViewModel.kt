package pi.restaurantapp.ui.fragments.management.workers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurantapp.model.fragments.management.AbstractItemListViewModel
import pi.restaurantapp.objects.data.user.UserBasic

class WorkersMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "users"

    override fun retrieveDataList(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, UserBasic>>() ?: HashMap()
        setDataList(data.toList().map { it.second }.toMutableList())
    }

}
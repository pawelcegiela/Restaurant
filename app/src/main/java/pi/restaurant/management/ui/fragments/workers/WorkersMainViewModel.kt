package pi.restaurant.management.ui.fragments.workers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.objects.data.user.UserBasic
import pi.restaurant.management.model.fragments.AbstractItemListViewModel

class WorkersMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "users"

    override fun retrieveDataList(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, UserBasic>>() ?: HashMap()
        setDataList(data.toList().map { it.second }.toMutableList())
    }

}
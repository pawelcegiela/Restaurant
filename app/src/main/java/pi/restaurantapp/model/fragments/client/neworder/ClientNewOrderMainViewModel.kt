package pi.restaurantapp.model.fragments.client.neworder

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurantapp.model.fragments.management.AbstractItemListViewModel
import pi.restaurantapp.objects.data.dish.DishBasic

class ClientNewOrderMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "dishes"
    var shouldDisplayFAB: Boolean = true

    override fun retrieveDataList(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, DishBasic>>() ?: HashMap()
        setDataList(data.toList().map { it.second }.toMutableList())
    }

    override fun displayFAB(): Boolean {
        return shouldDisplayFAB
    }
}
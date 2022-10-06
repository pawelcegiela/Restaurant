package pi.restaurant.management.logic.fragments.dishes

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.objects.data.dish.DishBasic
import pi.restaurant.management.logic.fragments.AbstractItemListViewModel

class DishesMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "dishes"

    override fun setDataList(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, DishBasic>>() ?: HashMap()
        liveDataList.value = data.toList().map { it.second }.toMutableList()
    }
}
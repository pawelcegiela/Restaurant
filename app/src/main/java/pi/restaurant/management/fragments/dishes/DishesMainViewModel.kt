package pi.restaurant.management.fragments.dishes

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.data.Dish
import pi.restaurant.management.fragments.AbstractItemListViewModel

class DishesMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "dishes"

    override fun setDataList(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, Dish>>() ?: return
        liveDataList.value = data.toList().map { it.second }.toMutableList()
    }
}
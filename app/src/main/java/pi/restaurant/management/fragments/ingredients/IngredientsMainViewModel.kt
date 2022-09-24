package pi.restaurant.management.fragments.ingredients

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.data.Ingredient
import pi.restaurant.management.fragments.AbstractItemListViewModel

class IngredientsMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "ingredients"

    override fun setDataList(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, Ingredient>>() ?: return
        liveDataList.value = data.toList().map { it.second }.toMutableList()
    }
}
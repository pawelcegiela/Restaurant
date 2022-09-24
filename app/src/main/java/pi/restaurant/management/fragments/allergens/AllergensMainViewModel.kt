package pi.restaurant.management.fragments.allergens

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.data.Allergen
import pi.restaurant.management.fragments.AbstractItemListViewModel

class AllergensMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "allergens"

    override fun setDataList(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, Allergen>>() ?: return
        liveDataList.value = data.toList().map { it.second }.toMutableList()
    }

}
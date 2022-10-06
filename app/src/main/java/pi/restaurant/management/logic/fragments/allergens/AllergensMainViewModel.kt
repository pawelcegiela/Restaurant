package pi.restaurant.management.logic.fragments.allergens

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.objects.data.allergen.AllergenBasic
import pi.restaurant.management.logic.fragments.AbstractItemListViewModel

class AllergensMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "allergens"

    override fun setDataList(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, AllergenBasic>>() ?: HashMap()
        liveDataList.value = data.toList().map { it.second }.toMutableList()
    }

}
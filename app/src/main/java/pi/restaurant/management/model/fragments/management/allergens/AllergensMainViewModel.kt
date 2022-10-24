package pi.restaurant.management.model.fragments.management.allergens

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.model.fragments.management.AbstractItemListViewModel
import pi.restaurant.management.objects.data.allergen.AllergenBasic

class AllergensMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "allergens"

    override fun retrieveDataList(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, AllergenBasic>>() ?: HashMap()
        setDataList(data.toList().map { it.second }.toMutableList())
    }

}
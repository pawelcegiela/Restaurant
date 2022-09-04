package pi.restaurant.management.fragments.allergens

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.adapters.AllergensRecyclerAdapter
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.Allergen
import pi.restaurant.management.fragments.AbstractItemListFragment

class AllergensMainFragment : AbstractItemListFragment() {
    override val databasePath = "allergens"
    override val addActionId = R.id.actionAllergensToAddAllergen
    override val editActionId = R.id.actionAllergensToEditAllergen

    override fun setData(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, Allergen>>() ?: return
        val list = data.toList().map { it.second }.toMutableList()
        binding.recyclerView.adapter =
            AllergensRecyclerAdapter(list, this@AllergensMainFragment)
        adapterData = list as MutableList<AbstractDataObject>
    }
}
package pi.restaurant.management.fragments.menu

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.adapters.MenuRecyclerAdapter
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.Dish
import pi.restaurant.management.fragments.AbstractItemListFragment

class MenuMainFragment : AbstractItemListFragment() {
    override val databasePath = "menu"
    override val addActionId = R.id.actionMenuToAddDish
    override val editActionId = R.id.actionMenuToEditDish

    override fun setData(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, Dish>>() ?: return
        val list = data.toList().map { it.second }.toMutableList()
        binding.recyclerView.adapter =
            MenuRecyclerAdapter(list, this@MenuMainFragment)
        adapterData = list as MutableList<AbstractDataObject>
    }
}
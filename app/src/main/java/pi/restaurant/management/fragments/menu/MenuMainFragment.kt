package pi.restaurant.management.fragments.menu

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.adapters.MenuRecyclerAdapter
import pi.restaurant.management.data.Dish
import pi.restaurant.management.fragments.RecyclerFragment

class MenuMainFragment : RecyclerFragment() {
    override val databasePath = "menu"
    override val fabActionId = R.id.actionMenuToAddDish

    override fun setData(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, Dish>>() ?: return
        val list = data.toList().map { it.second }
        binding.recyclerView.adapter =
            MenuRecyclerAdapter(list, this@MenuMainFragment)
    }
}
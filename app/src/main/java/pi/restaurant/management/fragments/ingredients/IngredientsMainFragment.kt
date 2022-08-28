package pi.restaurant.management.fragments.ingredients

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.adapters.IngredientsRecyclerAdapter
import pi.restaurant.management.data.Ingredient
import pi.restaurant.management.fragments.AbstractItemListFragment

class IngredientsMainFragment : AbstractItemListFragment() {
    override val databasePath = "ingredients"
    override val fabActionId = R.id.actionIngredientsToAddIngredient

    override fun setData(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, Ingredient>>() ?: return
        val list = data.toList().map { it.second }
        binding.recyclerView.adapter =
            IngredientsRecyclerAdapter(list, this@IngredientsMainFragment)
    }
}
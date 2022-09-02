package pi.restaurant.management.fragments.ingredients

import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.adapters.IngredientsRecyclerAdapter
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.Ingredient
import pi.restaurant.management.fragments.AbstractItemListFragment

class IngredientsMainFragment : AbstractItemListFragment() {
    override val databasePath = "ingredients"
    override val addActionId = R.id.actionIngredientsToAddIngredient
    override val editActionId = R.id.actionIngredientsToEditIngredient

    override fun setData(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, Ingredient>>() ?: return
        val list = data.toList().map { it.second }.toMutableList()
        binding.recyclerView.adapter =
            IngredientsRecyclerAdapter(list, this@IngredientsMainFragment)
        adapterData = list as MutableList<AbstractDataObject>
        binding.progress.progressBar.visibility = View.GONE
    }
}
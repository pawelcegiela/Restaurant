package pi.restaurant.management.fragments.dishes

import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.adapters.DishesRecyclerAdapter
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.Dish
import pi.restaurant.management.data.DishItem
import pi.restaurant.management.fragments.AbstractItemListFragment

class DishesMainFragment : AbstractItemListFragment() {
    override val databasePath = "dishes"
    override val addActionId = R.id.actionDishesToAddDish
    override val editActionId = R.id.actionDishesToEditDish

    override fun setData(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, Dish>>() ?: return
        val list = data.toList().map { it.second }.toMutableList()
        binding.recyclerView.adapter =
            DishesRecyclerAdapter(list, this@DishesMainFragment)
        adapterData = list as MutableList<AbstractDataObject>

        //TODO: Czy da się pominąć ten krok?
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<DishItem>("newItem")
            ?.observe(viewLifecycleOwner) {
                val navController = findNavController()
                navController.previousBackStackEntry?.savedStateHandle?.set("newItem", it)
                navController.popBackStack()
            }
    }
}
package pi.restaurant.management.ui.fragments.ingredients

import android.os.Bundle
import android.view.View
import pi.restaurant.management.objects.data.ingredient.IngredientBasic
import pi.restaurant.management.objects.enums.IngredientsTab
import pi.restaurant.management.ui.adapters.IngredientsRecyclerAdapter
import pi.restaurant.management.ui.fragments.ItemListSubFragment

class IngredientsItemListSubFragment(private var list: MutableList<IngredientBasic>, private val position: Int) :
    ItemListSubFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (position) {
            IngredientsTab.SUB_DISHES.ordinal -> list = list.filter { it.subDish }.toMutableList()
            IngredientsTab.INGREDIENTS.ordinal -> list = list.filter { !it.subDish }.toMutableList()
        }
        binding.recyclerView.adapter = IngredientsRecyclerAdapter(list, this)
    }
}
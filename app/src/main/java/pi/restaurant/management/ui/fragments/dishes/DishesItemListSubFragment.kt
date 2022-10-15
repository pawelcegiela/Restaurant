package pi.restaurant.management.ui.fragments.dishes

import android.os.Bundle
import android.view.View
import pi.restaurant.management.objects.data.dish.DishBasic
import pi.restaurant.management.objects.enums.DishType
import pi.restaurant.management.objects.enums.DishesTab
import pi.restaurant.management.ui.adapters.DishesRecyclerAdapter
import pi.restaurant.management.ui.fragments.ItemListSubFragment

class DishesItemListSubFragment(private var list: MutableList<DishBasic>, private val position: Int) : ItemListSubFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (position) {
            DishesTab.DISHES.ordinal -> list = list.filter { it.dishType == DishType.DISH.ordinal }.toMutableList()
            DishesTab.WARM_DISHES.ordinal -> list = list.filter { it.dishType == DishType.WARM_DISH.ordinal }.toMutableList()
            DishesTab.COLD_DISHES.ordinal -> list = list.filter { it.dishType == DishType.COLD_DISH.ordinal }.toMutableList()
            DishesTab.NON_ALCOHOLIC_DRINKS.ordinal -> list = list.filter { it.dishType == DishType.NON_ALCOHOLIC_DRINK.ordinal }.toMutableList()
            DishesTab.ALCOHOLIC_DRINKS.ordinal -> list = list.filter { it.dishType == DishType.ALCOHOLIC_DRINK.ordinal }.toMutableList()
        }
        binding.recyclerView.adapter = DishesRecyclerAdapter(list, this)
    }
}
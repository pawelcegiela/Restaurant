package pi.restaurantapp.ui.fragments.management.dishes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pi.restaurantapp.viewmodels.activities.management.DishesViewModel
import pi.restaurantapp.objects.data.dish.DishBasic
import pi.restaurantapp.objects.enums.DishType
import pi.restaurantapp.objects.enums.DishesTab
import pi.restaurantapp.ui.adapters.DishesRecyclerAdapter
import pi.restaurantapp.ui.fragments.ItemListSubFragment

class DishesItemListSubFragment(
    private var list: MutableList<DishBasic>,
    private val position: Int,
    fabFilter: FloatingActionButton,
    searchView: SearchView,
    private val showInactive: Boolean
) : ItemListSubFragment(fabFilter, searchView) {
    private val _activityViewModel: DishesViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        activityViewModel = _activityViewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (position) {
            DishesTab.DISHES.ordinal -> list = list.filter { it.dishType == DishType.DISH.ordinal }.toMutableList()
            DishesTab.WARM_DISHES.ordinal -> list = list.filter { it.dishType == DishType.WARM_DISH.ordinal }.toMutableList()
            DishesTab.COLD_DISHES.ordinal -> list = list.filter { it.dishType == DishType.COLD_DISH.ordinal }.toMutableList()
            DishesTab.NON_ALCOHOLIC_DRINKS.ordinal -> list =
                list.filter { it.dishType == DishType.NON_ALCOHOLIC_DRINK.ordinal }.toMutableList()
            DishesTab.ALCOHOLIC_DRINKS.ordinal -> list =
                list.filter { it.dishType == DishType.ALCOHOLIC_DRINK.ordinal }.toMutableList()
        }

        setAdapter()
    }

    private fun getFilteredList(): MutableList<DishBasic> {
        return if (showInactive) {
            if (_activityViewModel.getShowActive() && _activityViewModel.getShowDisabled()) {
                list
            } else if (!_activityViewModel.getShowActive() && !_activityViewModel.getShowDisabled()) {
                ArrayList()
            } else {
                list.filter { it.disabled == _activityViewModel.getShowDisabled() }.toMutableList()
            }
        } else {
            list.filter { !it.disabled && it.isActive }.toMutableList()
        }
    }

    override fun setAdapter() {
        binding.recyclerView.adapter = DishesRecyclerAdapter(getFilteredList(), this)
    }
}
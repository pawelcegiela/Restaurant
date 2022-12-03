package pi.restaurantapp.ui.fragments.management.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pi.restaurantapp.viewmodels.activities.management.IngredientsViewModel
import pi.restaurantapp.objects.data.ingredient.IngredientBasic
import pi.restaurantapp.objects.enums.IngredientsTab
import pi.restaurantapp.ui.adapters.IngredientsRecyclerAdapter
import pi.restaurantapp.ui.fragments.ItemListSubFragment

class IngredientsItemListSubFragment(
    private var list: MutableList<IngredientBasic>,
    private val position: Int,
    fabFilter: FloatingActionButton,
    searchView: SearchView
) :
    ItemListSubFragment(fabFilter, searchView) {
    private val _activityViewModel: IngredientsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        activityViewModel = _activityViewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (position) {
            IngredientsTab.SUB_DISHES.ordinal -> list = list.filter { it.subDish }.toMutableList()
            IngredientsTab.INGREDIENTS.ordinal -> list = list.filter { !it.subDish }.toMutableList()
        }

        setAdapter()
    }

    private fun getFilteredList(): MutableList<IngredientBasic> {
        return if (_activityViewModel.getShowActive() && _activityViewModel.getShowDisabled()) {
            list
        } else if (!_activityViewModel.getShowActive() && !_activityViewModel.getShowDisabled()) {
            ArrayList()
        } else {
            list.filter { it.disabled == _activityViewModel.getShowDisabled() }.toMutableList()
        }
    }

    override fun setAdapter() {
        binding.recyclerView.adapter = IngredientsRecyclerAdapter(getFilteredList(), this)
    }
}
package pi.restaurant.management.ui.fragments.ingredients

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pi.restaurant.management.model.activities.IngredientsViewModel
import pi.restaurant.management.objects.data.ingredient.IngredientBasic
import pi.restaurant.management.objects.enums.IngredientsTab
import pi.restaurant.management.ui.adapters.IngredientsRecyclerAdapter
import pi.restaurant.management.ui.fragments.ItemListSubFragment

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
        Log.e("Setting adapter", this.javaClass.name + " # " + position)
        binding.recyclerView.adapter = IngredientsRecyclerAdapter(getFilteredList(), this)
    }
}
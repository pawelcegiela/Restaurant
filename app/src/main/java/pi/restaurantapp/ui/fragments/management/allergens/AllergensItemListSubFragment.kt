package pi.restaurantapp.ui.fragments.management.allergens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pi.restaurantapp.model.activities.management.IngredientsViewModel
import pi.restaurantapp.objects.data.allergen.AllergenBasic
import pi.restaurantapp.ui.adapters.AllergensRecyclerAdapter
import pi.restaurantapp.ui.fragments.ItemListSubFragment

class AllergensItemListSubFragment(private val list: MutableList<AllergenBasic>, fabFilter: FloatingActionButton, searchView: SearchView) :
    ItemListSubFragment(fabFilter, searchView) {
    private val _activityViewModel: IngredientsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        activityViewModel = _activityViewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
    }

    private fun getFilteredList(): MutableList<AllergenBasic> {
        return if (_activityViewModel.getShowActive() && _activityViewModel.getShowDisabled()) {
            list
        } else if (!_activityViewModel.getShowActive() && !_activityViewModel.getShowDisabled()) {
            ArrayList()
        } else {
            list.filter { it.disabled == _activityViewModel.getShowDisabled() }.toMutableList()
        }
    }

    override fun setAdapter() {
        Log.e("Setting adapter", this.javaClass.name)
        binding.recyclerView.adapter = AllergensRecyclerAdapter(getFilteredList(), this)
    }
}
package pi.restaurantapp.ui.fragments.client.discounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.ui.adapters.DiscountsRecyclerAdapter
import pi.restaurantapp.ui.fragments.ItemListSubFragment
import pi.restaurantapp.viewmodels.activities.client.ClientDiscountsViewModel

class ClientDiscountsItemListSubFragment(
    private var list: MutableList<DiscountBasic>,
    fabFilter: FloatingActionButton,
    searchView: SearchView
) : ItemListSubFragment(fabFilter, searchView) {
    private val _activityViewModel: ClientDiscountsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        activityViewModel = _activityViewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
    }

    private fun getFilteredList(): MutableList<DiscountBasic> {
        return if (_activityViewModel.getShowActive() && _activityViewModel.getShowDisabled()) {
            list
        } else if (!_activityViewModel.getShowActive() && !_activityViewModel.getShowDisabled()) {
            ArrayList()
        } else {
            list.filter { it.disabled == _activityViewModel.getShowDisabled() }.toMutableList()
        }
    }

    override fun setAdapter() {
        binding.recyclerView.adapter = DiscountsRecyclerAdapter(getFilteredList(), this)
    }
}
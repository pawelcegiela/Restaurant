package pi.restaurant.management.ui.fragments.discounts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pi.restaurant.management.model.activities.DiscountsViewModel
import pi.restaurant.management.objects.data.discount.DiscountBasic
import pi.restaurant.management.objects.enums.DiscountsTab
import pi.restaurant.management.ui.adapters.DiscountsRecyclerAdapter
import pi.restaurant.management.ui.fragments.ItemListSubFragment
import java.util.*

class DiscountsItemListSubFragment(
    private var list: MutableList<DiscountBasic>,
    private val position: Int,
    fabFilter: FloatingActionButton
) : ItemListSubFragment(fabFilter) {
    private val _activityViewModel: DiscountsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        activityViewModel = _activityViewModel
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (position) {
            DiscountsTab.ACTIVE.ordinal -> list = list.filter { it.expirationDate >= Date() }.toMutableList()
            DiscountsTab.EXPIRED.ordinal -> list = list.filter { it.expirationDate < Date() }.toMutableList()
        }

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
        Log.e("Setting adapter", this.javaClass.name + " # " + position)
        binding.recyclerView.adapter = DiscountsRecyclerAdapter(getFilteredList(), this)
    }
}
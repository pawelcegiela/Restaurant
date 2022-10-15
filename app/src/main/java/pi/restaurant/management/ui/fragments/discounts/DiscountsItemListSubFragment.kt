package pi.restaurant.management.ui.fragments.discounts

import android.os.Bundle
import android.view.View
import pi.restaurant.management.objects.data.discount.DiscountBasic
import pi.restaurant.management.objects.enums.DiscountsTab
import pi.restaurant.management.ui.adapters.DiscountsRecyclerAdapter
import pi.restaurant.management.ui.fragments.ItemListSubFragment
import java.util.*

class DiscountsItemListSubFragment(private var list: MutableList<DiscountBasic>, private val position: Int) : ItemListSubFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (position) {
            DiscountsTab.ACTIVE.ordinal -> list = list.filter { it.expirationDate >= Date() }.toMutableList()
            DiscountsTab.EXPIRED.ordinal -> list = list.filter { it.expirationDate < Date() }.toMutableList()
        }
        binding.recyclerView.adapter = DiscountsRecyclerAdapter(list, this)
    }
}
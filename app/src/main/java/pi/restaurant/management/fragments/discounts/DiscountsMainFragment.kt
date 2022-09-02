package pi.restaurant.management.fragments.discounts

import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.adapters.DiscountsRecyclerAdapter
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.DiscountGroup
import pi.restaurant.management.fragments.AbstractItemListFragment

class DiscountsMainFragment : AbstractItemListFragment() {
    override val databasePath = "discounts"
    override val addActionId = R.id.actionDiscountsToAddDiscount
    override val editActionId = R.id.actionDiscountsToEditDiscount

    override fun setData(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, DiscountGroup>>() ?: return
        val list = data.toList().map { it.second }.toMutableList()
        binding.recyclerView.adapter =
            DiscountsRecyclerAdapter(list, this@DiscountsMainFragment)
        adapterData = list as MutableList<AbstractDataObject>
        binding.progress.progressBar.visibility = View.GONE
    }
}
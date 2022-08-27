package pi.restaurant.management.fragments.discounts

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.adapters.DiscountsRecyclerAdapter
import pi.restaurant.management.data.DiscountGroup
import pi.restaurant.management.fragments.RecyclerFragment

class DiscountsMainFragment : RecyclerFragment() {
    override val databasePath = "discounts"
    override val fabActionId = R.id.actionDiscountsToAddDiscount

    override fun setData(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, DiscountGroup>>() ?: return
        val list = data.toList().map { it.second }
        binding.recyclerView.adapter =
            DiscountsRecyclerAdapter(list, this@DiscountsMainFragment)
    }
}
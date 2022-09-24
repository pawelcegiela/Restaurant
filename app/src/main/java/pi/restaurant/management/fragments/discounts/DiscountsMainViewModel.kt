package pi.restaurant.management.fragments.discounts

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.data.DiscountGroup
import pi.restaurant.management.fragments.AbstractItemListViewModel

class DiscountsMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "discounts"

    override fun setDataList(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, DiscountGroup>>() ?: return
        liveDataList.value = data.toList().map { it.second }.toMutableList()
    }
}
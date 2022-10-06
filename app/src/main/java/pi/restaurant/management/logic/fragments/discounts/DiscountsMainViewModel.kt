package pi.restaurant.management.logic.fragments.discounts

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.objects.data.discount.DiscountBasic
import pi.restaurant.management.logic.fragments.AbstractItemListViewModel

class DiscountsMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "discounts"

    override fun setDataList(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, DiscountBasic>>() ?: HashMap()
        liveDataList.value = data.toList().map { it.second }.toMutableList()
    }
}
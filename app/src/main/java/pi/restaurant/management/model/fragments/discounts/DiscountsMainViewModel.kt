package pi.restaurant.management.model.fragments.discounts

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.objects.data.discount.DiscountBasic
import pi.restaurant.management.model.fragments.AbstractItemListViewModel

class DiscountsMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "discounts"

    override fun retrieveDataList(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, DiscountBasic>>() ?: HashMap()
        setDataList(data.toList().map { it.second }.toMutableList())
    }
}
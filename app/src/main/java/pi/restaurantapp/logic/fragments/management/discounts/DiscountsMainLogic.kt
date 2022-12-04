package pi.restaurantapp.logic.fragments.management.discounts

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.AbstractItemListLogic
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.discount.DiscountBasic

class DiscountsMainLogic : AbstractItemListLogic() {
    override val databasePath = "discounts"

    override fun retrieveDataList(snapshot: QuerySnapshot, callback: (MutableList<AbstractDataObject>) -> Unit) {
        val dataList = snapshot.map { document -> document.toObject<DiscountBasic>() }.toMutableList<AbstractDataObject>()
        callback(dataList)
    }
}
package pi.restaurantapp.model.fragments.management.discounts

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.model.fragments.AbstractItemListViewModel
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.discount.DiscountBasic

class DiscountsMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "discounts"

    override fun retrieveDataList(snapshot: QuerySnapshot) {
        val dataList = snapshot.map { document -> document.toObject<DiscountBasic>() }.toMutableList<AbstractDataObject>()
        setDataList(dataList)
    }
}
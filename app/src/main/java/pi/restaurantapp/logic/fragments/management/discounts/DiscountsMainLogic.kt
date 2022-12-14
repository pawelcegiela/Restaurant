package pi.restaurantapp.logic.fragments.management.discounts

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.AbstractItemListLogic
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.discount.DiscountBasic

/**
 * Class responsible for business logic and communication with database (Model layer) for DiscountsMainFragment.
 * @see pi.restaurantapp.ui.fragments.management.discounts.DiscountsMainFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.discounts.AddDiscountViewModel ViewModel layer
 */
class DiscountsMainLogic : AbstractItemListLogic() {
    override val databasePath = "discounts"

    override fun retrieveDataList(snapshot: QuerySnapshot, callback: (MutableList<AbstractDataObject>) -> Unit) {
        val dataList = snapshot.map { document -> document.toObject<DiscountBasic>() }.sortedByDescending { it.expirationDate }.toMutableList<AbstractDataObject>()
        callback(dataList)
    }
}
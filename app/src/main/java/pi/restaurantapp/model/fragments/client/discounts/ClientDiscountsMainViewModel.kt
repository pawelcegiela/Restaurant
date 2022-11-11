package pi.restaurantapp.model.fragments.client.discounts

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.model.fragments.AbstractItemListViewModel
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.discount.DiscountBasic

// TODO Filtrowanie - tylko aktywne + danego użytkownika
class ClientDiscountsMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "discounts"

    override fun retrieveDataList(snapshot: QuerySnapshot) {
        val dataList = snapshot.map { document -> document.toObject<DiscountBasic>() }.toMutableList<AbstractDataObject>()
        setDataList(dataList)
    }
}
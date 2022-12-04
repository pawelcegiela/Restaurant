package pi.restaurantapp.logic.fragments.management.dishes

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.AbstractItemListLogic
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.dish.DishBasic

class DishesMainLogic : AbstractItemListLogic() {
    override val databasePath = "dishes"

    override fun retrieveDataList(snapshot: QuerySnapshot, callback: (MutableList<AbstractDataObject>) -> Unit) {
        val dataList = snapshot.map { document -> document.toObject<DishBasic>() }.toMutableList<AbstractDataObject>()
        callback(dataList)
    }
}
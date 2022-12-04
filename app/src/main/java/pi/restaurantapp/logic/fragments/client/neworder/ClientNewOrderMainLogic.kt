package pi.restaurantapp.logic.fragments.client.neworder

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.AbstractItemListLogic
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.dish.DishBasic

class ClientNewOrderMainLogic : AbstractItemListLogic() {
    override val databasePath = "dishes"

    override fun retrieveDataList(snapshot: QuerySnapshot, callback: (MutableList<AbstractDataObject>) -> Unit) {
        val dataList: ArrayList<AbstractDataObject> = ArrayList()
        for (document in snapshot) {
            dataList.add(document.toObject<DishBasic>())
        }
        callback(dataList)
    }
}
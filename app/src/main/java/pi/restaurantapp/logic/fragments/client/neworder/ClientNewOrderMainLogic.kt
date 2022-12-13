package pi.restaurantapp.logic.fragments.client.neworder

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.AbstractItemListLogic
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.dish.DishBasic

/**
 * Class responsible for business logic and communication with database (Model layer) for ClientNewOrderMainFragment.
 * @see pi.restaurantapp.ui.fragments.client.neworder.ClientNewOrderMainFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.client.neworder.ClientNewOrderMainViewModel ViewModel layer
 */
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
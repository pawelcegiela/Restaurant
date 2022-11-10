package pi.restaurantapp.model.fragments.client.neworder

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.model.fragments.AbstractItemListViewModel
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.dish.DishBasic

class ClientNewOrderMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "dishes"
    var shouldDisplayFAB: Boolean = true

    override fun retrieveDataList(snapshot: QuerySnapshot) {
        val dataList: ArrayList<AbstractDataObject> = ArrayList()
        for (document in snapshot) {
            dataList.add(document.toObject<DishBasic>())
        }
        setDataList(dataList)
    }

    override fun displayFAB(): Boolean {
        return shouldDisplayFAB
    }
}
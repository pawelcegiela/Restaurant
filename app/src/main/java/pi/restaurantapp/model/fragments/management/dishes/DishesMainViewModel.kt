package pi.restaurantapp.model.fragments.management.dishes

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.model.fragments.AbstractItemListViewModel
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.dish.DishBasic

class DishesMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "dishes"
    var shouldDisplayFAB: Boolean = true

    override fun retrieveDataList(snapshot: QuerySnapshot) {
        val dataList = snapshot.map { document -> document.toObject<DishBasic>() }.toMutableList<AbstractDataObject>()
        setDataList(dataList)
    }

    override fun displayFAB(): Boolean {
        return shouldDisplayFAB
    }
}
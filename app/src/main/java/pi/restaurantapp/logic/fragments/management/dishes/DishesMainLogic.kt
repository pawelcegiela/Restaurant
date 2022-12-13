package pi.restaurantapp.logic.fragments.management.dishes

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.AbstractItemListLogic
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.dish.DishBasic

/**
 * Class responsible for business logic and communication with database (Model layer) for DishesMainFragment.
 * @see pi.restaurantapp.ui.fragments.management.dishes.DishesMainFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.dishes.DishesMainViewModel ViewModel layer
 */
class DishesMainLogic : AbstractItemListLogic() {
    override val databasePath = "dishes"

    override fun retrieveDataList(snapshot: QuerySnapshot, callback: (MutableList<AbstractDataObject>) -> Unit) {
        val dataList = snapshot.map { document -> document.toObject<DishBasic>() }.toMutableList<AbstractDataObject>()
        callback(dataList)
    }
}
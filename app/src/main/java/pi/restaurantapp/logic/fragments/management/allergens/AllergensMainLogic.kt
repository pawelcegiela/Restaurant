package pi.restaurantapp.logic.fragments.management.allergens

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.AbstractItemListLogic
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.allergen.AllergenBasic

/**
 * Class responsible for business logic and communication with database (Model layer) for AllergensMainFragment.
 * @see pi.restaurantapp.ui.fragments.management.allergens.AllergensMainFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.allergens.AllergensMainViewModel ViewModel layer
 */
class AllergensMainLogic : AbstractItemListLogic() {
    override val databasePath = "allergens"

    override fun retrieveDataList(snapshot: QuerySnapshot, callback: (MutableList<AbstractDataObject>) -> Unit) {
        val dataList = snapshot.map { document -> document.toObject<AllergenBasic>() }.toMutableList<AbstractDataObject>()
        callback(dataList)
    }
}
package pi.restaurantapp.model.fragments.management.allergens

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.model.fragments.AbstractItemListViewModel
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.allergen.AllergenBasic

class AllergensMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "allergens"

    override fun retrieveDataList(snapshot: QuerySnapshot) {
        val dataList = snapshot.map { document -> document.toObject<AllergenBasic>() }.toMutableList<AbstractDataObject>()
        setDataList(dataList)
    }

}
package pi.restaurantapp.logic.fragments.management.ingredients

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.AbstractItemListLogic
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.ingredient.IngredientBasic

class IngredientsMainLogic : AbstractItemListLogic() {
    override val databasePath = "ingredients"

    override fun retrieveDataList(snapshot: QuerySnapshot, callback: (MutableList<AbstractDataObject>) -> Unit) {
        val dataList = snapshot.map { document -> document.toObject<IngredientBasic>() }.toMutableList<AbstractDataObject>()
        callback(dataList)
    }

}
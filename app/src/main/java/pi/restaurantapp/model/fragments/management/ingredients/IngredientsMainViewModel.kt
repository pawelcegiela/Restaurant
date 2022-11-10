package pi.restaurantapp.model.fragments.management.ingredients

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.model.fragments.AbstractItemListViewModel
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.ingredient.IngredientBasic

class IngredientsMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "ingredients"

    override fun retrieveDataList(snapshot: QuerySnapshot) {
        val dataList = snapshot.map { document -> document.toObject<IngredientBasic>() }.toMutableList<AbstractDataObject>()
        setDataList(dataList)
    }
}
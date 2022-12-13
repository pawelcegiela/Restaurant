package pi.restaurantapp.logic.fragments.management.ingredients

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.AbstractItemListLogic
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.ingredient.IngredientBasic

/**
 * Class responsible for business logic and communication with database (Model layer) for IngredientsMainFragment.
 * @see pi.restaurantapp.ui.fragments.management.ingredients.IngredientsMainFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.ingredients.IngredientsMainViewModel ViewModel layer
 */
class IngredientsMainLogic : AbstractItemListLogic() {
    override val databasePath = "ingredients"

    override fun retrieveDataList(snapshot: QuerySnapshot, callback: (MutableList<AbstractDataObject>) -> Unit) {
        val dataList = snapshot.map { document -> document.toObject<IngredientBasic>() }.toMutableList<AbstractDataObject>()
        callback(dataList)
    }

}
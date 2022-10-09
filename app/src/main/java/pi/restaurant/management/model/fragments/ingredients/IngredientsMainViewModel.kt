package pi.restaurant.management.model.fragments.ingredients

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.objects.data.ingredient.IngredientBasic
import pi.restaurant.management.model.fragments.AbstractItemListViewModel

class IngredientsMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "ingredients"

    override fun retrieveDataList(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, IngredientBasic>>() ?: HashMap()
        setDataList(data.toList().map { it.second }.toMutableList())
    }
}
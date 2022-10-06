package pi.restaurant.management.logic.fragments.ingredients

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.objects.data.ingredient.IngredientBasic
import pi.restaurant.management.logic.fragments.AbstractItemListViewModel

class IngredientsMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "ingredients"

    override fun setDataList(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, IngredientBasic>>() ?: HashMap()
        liveDataList.value = data.toList().map { it.second }.toMutableList()
    }
}
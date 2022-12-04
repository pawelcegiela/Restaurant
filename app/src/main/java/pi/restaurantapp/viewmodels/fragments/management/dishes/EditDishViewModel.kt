package pi.restaurantapp.viewmodels.fragments.management.dishes

import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.management.dishes.EditDishLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.dish.Dish
import pi.restaurantapp.objects.data.dish.DishBasic
import pi.restaurantapp.objects.data.dish.DishDetails

class EditDishViewModel : AbstractModifyDishViewModel() {
    override val logic = EditDishLogic()

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<DishBasic>() ?: DishBasic()
        val details = snapshotsPair.details?.toObject<DishDetails>() ?: DishDetails()
        setItem(Dish(itemId, basic, details))
        logic.item = Dish(itemId, basic, details)

        observer.baseIngredients = details.baseIngredients.map { it.value }.toMutableList()
        observer.otherIngredients = details.otherIngredients.map { it.value }.toMutableList()
        observer.possibleIngredients = details.possibleIngredients.map { it.value }.toMutableList()
        observer.allergens = details.allergens.map { it.value }.toMutableList()
    }


}
package pi.restaurantapp.viewmodels.fragments.management.dishes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.management.dishes.PreviewDishLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.allergen.AllergenBasic
import pi.restaurantapp.objects.data.dish.Dish
import pi.restaurantapp.objects.data.dish.DishBasic
import pi.restaurantapp.objects.data.dish.DishDetails
import pi.restaurantapp.objects.data.ingredient.IngredientItem
import pi.restaurantapp.objects.enums.IngredientStatus
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel

class PreviewDishViewModel : AbstractPreviewItemViewModel() {
    override val logic = PreviewDishLogic()

    private val _item: MutableLiveData<Dish> = MutableLiveData()
    val item: LiveData<Dish> = _item

    val baseOtherIngredients = ArrayList<Pair<IngredientItem, IngredientStatus>>()
    val possibleIngredients = ArrayList<Pair<IngredientItem, IngredientStatus>>()
    val allergens = ArrayList<AllergenBasic>()

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<DishBasic>() ?: DishBasic()
        val details = snapshotsPair.details?.toObject<DishDetails>() ?: DishDetails()
        _item.value = Dish(itemId, basic, details)

        baseOtherIngredients.addAll(details.baseIngredients.toList().map { it.second to IngredientStatus.BASE })
        baseOtherIngredients.addAll(details.otherIngredients.toList().map { it.second to IngredientStatus.OTHER })
        possibleIngredients.addAll(details.possibleIngredients.toList().map { it.second to IngredientStatus.POSSIBLE })
        allergens.addAll(details.allergens.toList().map { it.second })
    }

    override fun isDisabled(): Boolean {
        return item.value?.basic?.disabled == true
    }
}
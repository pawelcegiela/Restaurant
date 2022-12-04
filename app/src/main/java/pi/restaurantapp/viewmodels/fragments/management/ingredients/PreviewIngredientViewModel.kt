package pi.restaurantapp.viewmodels.fragments.management.ingredients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.management.ingredients.PreviewIngredientLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.ingredient.Ingredient
import pi.restaurantapp.objects.data.ingredient.IngredientAmountChange
import pi.restaurantapp.objects.data.ingredient.IngredientBasic
import pi.restaurantapp.objects.data.ingredient.IngredientDetails
import pi.restaurantapp.objects.enums.IngredientModificationType
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel
import java.lang.Integer.max

class PreviewIngredientViewModel : AbstractPreviewItemViewModel() {
    override val logic = PreviewIngredientLogic()

    var containingDishes = ArrayList<String>()
    var containingSubDishes = ArrayList<String>()

    private val _item: MutableLiveData<Ingredient> = MutableLiveData()
    val item: LiveData<Ingredient> = _item

    var amountChanges: MutableList<IngredientAmountChange> = ArrayList()

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<IngredientBasic>() ?: IngredientBasic()
        val details = snapshotsPair.details?.toObject<IngredientDetails>() ?: IngredientDetails()
        _item.value = Ingredient(itemId, basic, details)

        getAmountChanges(details.amountChanges)

        logic.getContainingDishes(
            details.containingDishes.map { it.key },
            details.containingSubDishes.map { it.key }) { _containingDishes, _containingSubDishes ->
            containingDishes = _containingDishes
            containingSubDishes = _containingSubDishes
            setReadyToUnlock()
        }
    }

    private fun getAmountChanges(amountChangesHashMap: HashMap<String, IngredientAmountChange>) {
        val firstIndex = max(amountChangesHashMap.size - 10, 0)
        val lastIndex = amountChangesHashMap.size
        amountChanges = amountChangesHashMap.map { it.value }.sortedByDescending { it.date }.subList(firstIndex, lastIndex).toMutableList()
    }

    override fun isDisabled(): Boolean {
        return item.value?.basic?.disabled == true
    }

    fun updateIngredientAmount(
        id: String,
        _amount: Int,
        modificationType: IngredientModificationType,
        setNewAmount: (Int) -> (Unit),
        addNewAmountChange: (IngredientAmountChange) -> (Unit)
    ) {
        logic.updateIngredientAmount(id, _amount, modificationType, setNewAmount, addNewAmountChange)
    }

}
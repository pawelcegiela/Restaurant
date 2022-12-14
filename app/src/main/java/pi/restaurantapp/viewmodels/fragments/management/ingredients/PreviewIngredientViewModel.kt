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
import java.lang.Integer.min

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for PreviewIngredientFragment.
 * @see pi.restaurantapp.logic.fragments.management.ingredients.PreviewIngredientLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.ingredients.PreviewIngredientFragment View layer
 */
class PreviewIngredientViewModel : AbstractPreviewItemViewModel() {
    override val logic = PreviewIngredientLogic()

    var containingDishes = MutableLiveData<ArrayList<String>>(ArrayList())
    var containingSubDishes = MutableLiveData<ArrayList<String>>(ArrayList())

    private val _item: MutableLiveData<Ingredient> = MutableLiveData()
    val item: LiveData<Ingredient> = _item

    var amountChanges = MutableLiveData<ArrayList<IngredientAmountChange>>(ArrayList())

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<IngredientBasic>() ?: IngredientBasic()
        val details = snapshotsPair.details?.toObject<IngredientDetails>() ?: IngredientDetails()
        _item.value = Ingredient(itemId, basic, details)

        setAmountChanges(details.amountChanges)

        logic.getContainingDishes(
            details.containingDishes.map { it.key },
            details.containingSubDishes.map { it.key }) { _containingDishes, _containingSubDishes ->
            containingDishes.value?.addAll(_containingDishes)
            containingSubDishes.value?.addAll(_containingSubDishes)
            setReadyToUnlock()
        }
    }

    private fun setAmountChanges(amountChangesHashMap: HashMap<String, IngredientAmountChange>) {
        val lastIndex = min(9, amountChangesHashMap.size)
        amountChanges.value?.addAll(amountChangesHashMap.map { it.value }.sortedByDescending { it.date }.subList(0, lastIndex))
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
        logic.updateIngredientAmount(
            id,
            item.value?.basic?.name ?: "",
            _amount,
            modificationType,
            item.value?.details?.containingDishes?.map { it.key } ?: ArrayList(),
            item.value?.details?.disableDishOnShortage ?: false,
            setNewAmount,
            addNewAmountChange)
    }

}
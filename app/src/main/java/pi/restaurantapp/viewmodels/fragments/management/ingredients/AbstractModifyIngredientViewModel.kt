package pi.restaurantapp.viewmodels.fragments.management.ingredients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.logic.fragments.management.ingredients.AbstractModifyIngredientLogic
import pi.restaurantapp.objects.data.NullableSplitDataObject
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.ingredient.Ingredient
import pi.restaurantapp.objects.data.ingredient.IngredientBasic
import pi.restaurantapp.objects.data.ingredient.IngredientItem
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel

/**
 * Abstract class responsible for presentation logic and binding between data/model and view (ViewModel layer) for AbstractModifyIngredientFragment.
 * @see pi.restaurantapp.logic.fragments.management.ingredients.AbstractModifyIngredientLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.ingredients.AbstractModifyIngredientFragment View layer
 */
abstract class AbstractModifyIngredientViewModel : AbstractModifyItemViewModel() {
    private val _logic: AbstractModifyIngredientLogic get() = logic as AbstractModifyIngredientLogic

    private val _item: MutableLiveData<Ingredient> = MutableLiveData()
    val item: LiveData<Ingredient> = _item

    override val splitDataObject get() = NullableSplitDataObject(itemId, item.value?.basic, item.value?.details)

    private val _allIngredients = MutableLiveData<MutableList<IngredientBasic>>(ArrayList())
    val allIngredients: LiveData<MutableList<IngredientBasic>> = _allIngredients

    private var previousSubIngredients: MutableList<IngredientItem> = ArrayList()

    var observer = ModifyIngredientObserver(_item)

    override fun saveToDatabase2(data: SplitDataObject) {
        _logic.saveToDatabase(data, previousSubIngredients) { saveStatus ->
            setSaveStatus(saveStatus)
        }
    }

    fun setItem(newItem: Ingredient) {
        _item.value = newItem
        newItem.details.subIngredients?.let { previousSubIngredients.addAll(it) }
        _logic.getAllIngredients { allIngredients ->
            _allIngredients.value?.addAll(allIngredients)
        }
    }

}
package pi.restaurantapp.viewmodels.fragments.management.allergens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.objects.data.NullableSplitDataObject
import pi.restaurantapp.objects.data.allergen.Allergen
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel

/**
 * Abstract class responsible for presentation logic and binding between data/model and view (ViewModel layer) for AbstractModifyAllergenFragment.
 * @see pi.restaurantapp.logic.fragments.management.allergens.AbstractModifyAllergenLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.allergens.AbstractModifyAllergenFragment View layer
 */
abstract class AbstractModifyAllergenViewModel : AbstractModifyItemViewModel() {
    private val _item: MutableLiveData<Allergen> = MutableLiveData()
    val item: LiveData<Allergen> = _item

    override val splitDataObject get() = NullableSplitDataObject(itemId, item.value?.basic, item.value?.details)

    fun setItem(newItem: Allergen) {
        _item.value = newItem
    }
}
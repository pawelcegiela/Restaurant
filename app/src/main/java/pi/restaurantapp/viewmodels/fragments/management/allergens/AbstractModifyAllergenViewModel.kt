package pi.restaurantapp.viewmodels.fragments.management.allergens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.objects.data.allergen.Allergen
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel

abstract class AbstractModifyAllergenViewModel : AbstractModifyItemViewModel() {
    private val _item: MutableLiveData<Allergen> = MutableLiveData()
    val item: LiveData<Allergen> = _item

    fun setItem(newItem: Allergen) {
        _item.value = newItem
    }
}
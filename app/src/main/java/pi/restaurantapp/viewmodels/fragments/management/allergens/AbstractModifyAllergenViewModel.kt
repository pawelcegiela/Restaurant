package pi.restaurantapp.viewmodels.fragments.management.allergens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.objects.data.allergen.Allergen

abstract class AbstractModifyAllergenViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "allergens"

    private val _item: MutableLiveData<Allergen> = MutableLiveData()
    val item: LiveData<Allergen> = _item

    fun setItem(newItem: Allergen) {
        _item.value = newItem
    }
}
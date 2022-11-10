package pi.restaurantapp.model.fragments.management.allergens

import pi.restaurantapp.model.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.objects.data.allergen.Allergen
import pi.restaurantapp.objects.data.allergen.AllergenBasic
import pi.restaurantapp.objects.data.allergen.AllergenDetails

abstract class AbstractModifyAllergenViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "allergens"

    fun getPreviousItem(): Allergen {
        if (this is EditAllergenViewModel) {
            return item.value ?: Allergen(itemId, AllergenBasic(), AllergenDetails())
        }
        return Allergen(itemId, AllergenBasic(), AllergenDetails())
    }
}
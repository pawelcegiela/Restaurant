package pi.restaurant.management.model.fragments.management.allergens

import pi.restaurant.management.model.fragments.management.AbstractModifyItemViewModel
import pi.restaurant.management.objects.data.allergen.Allergen
import pi.restaurant.management.objects.data.allergen.AllergenBasic
import pi.restaurant.management.objects.data.allergen.AllergenDetails

abstract class AbstractModifyAllergenViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "allergens"

    fun getPreviousItem(): Allergen {
        if (this is EditAllergenViewModel) {
            return item.value ?: Allergen(itemId, AllergenBasic(), AllergenDetails())
        }
        return Allergen(itemId, AllergenBasic(), AllergenDetails())
    }
}
package pi.restaurant.management.logic.fragments.allergens

import pi.restaurant.management.logic.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.objects.data.allergen.Allergen

abstract class AbstractModifyAllergenViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "allergens"
    var allergen: Allergen? = null

}
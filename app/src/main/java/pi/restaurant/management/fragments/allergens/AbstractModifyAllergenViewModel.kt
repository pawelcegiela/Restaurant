package pi.restaurant.management.fragments.allergens

import pi.restaurant.management.fragments.AbstractModifyItemViewModel

abstract class AbstractModifyAllergenViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "allergens"

}
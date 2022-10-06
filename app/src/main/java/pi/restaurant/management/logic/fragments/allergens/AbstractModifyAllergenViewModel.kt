package pi.restaurant.management.logic.fragments.allergens

import pi.restaurant.management.logic.fragments.AbstractModifyItemViewModel

abstract class AbstractModifyAllergenViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "allergens"

}
package pi.restaurantapp.logic.fragments.management.allergens

import pi.restaurantapp.logic.fragments.AbstractModifyItemLogic

abstract class AbstractModifyAllergenLogic : AbstractModifyItemLogic() {
    override val databasePath = "allergens"
}
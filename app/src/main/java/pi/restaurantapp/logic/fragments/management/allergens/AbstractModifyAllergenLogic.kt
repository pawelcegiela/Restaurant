package pi.restaurantapp.logic.fragments.management.allergens

import pi.restaurantapp.logic.fragments.AbstractModifyItemLogic

/**
 * Abstract class responsible for business logic and communication with database (Model layer) for AbstractModifyAllergenFragment.
 * @see pi.restaurantapp.ui.fragments.management.allergens.AbstractModifyAllergenFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.allergens.AbstractModifyAllergenViewModel ViewModel layer
 */
abstract class AbstractModifyAllergenLogic : AbstractModifyItemLogic() {
    override val databasePath = "allergens"
}
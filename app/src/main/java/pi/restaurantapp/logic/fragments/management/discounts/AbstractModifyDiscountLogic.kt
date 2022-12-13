package pi.restaurantapp.logic.fragments.management.discounts

import pi.restaurantapp.logic.fragments.AbstractModifyItemLogic

/**
 * Abstract class responsible for business logic and communication with database (Model layer) for AbstractModifyDiscountFragment.
 * @see pi.restaurantapp.ui.fragments.management.discounts.AbstractModifyDiscountFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.discounts.AbstractModifyDiscountViewModel ViewModel layer
 */
abstract class AbstractModifyDiscountLogic : AbstractModifyItemLogic() {
    override val databasePath = "discounts"
}
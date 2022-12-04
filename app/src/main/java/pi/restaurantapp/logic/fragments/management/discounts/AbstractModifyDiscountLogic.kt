package pi.restaurantapp.logic.fragments.management.discounts

import pi.restaurantapp.logic.fragments.AbstractModifyItemLogic

abstract class AbstractModifyDiscountLogic : AbstractModifyItemLogic() {
    override val databasePath = "discounts"
}
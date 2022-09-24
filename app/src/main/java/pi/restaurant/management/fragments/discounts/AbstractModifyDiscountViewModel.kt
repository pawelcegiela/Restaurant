package pi.restaurant.management.fragments.discounts

import pi.restaurant.management.fragments.AbstractModifyItemViewModel

abstract class AbstractModifyDiscountViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "discounts"

}
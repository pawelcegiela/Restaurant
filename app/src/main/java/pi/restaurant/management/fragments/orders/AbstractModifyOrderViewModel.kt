package pi.restaurant.management.fragments.orders

import pi.restaurant.management.fragments.AbstractModifyItemViewModel

abstract class AbstractModifyOrderViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "orders"

}
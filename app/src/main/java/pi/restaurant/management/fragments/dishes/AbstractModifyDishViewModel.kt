package pi.restaurant.management.fragments.dishes

import pi.restaurant.management.fragments.AbstractModifyItemViewModel

abstract class AbstractModifyDishViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "dishes"
}
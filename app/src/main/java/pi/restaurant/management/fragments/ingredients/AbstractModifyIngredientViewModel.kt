package pi.restaurant.management.fragments.ingredients

import pi.restaurant.management.fragments.AbstractModifyItemViewModel

abstract class AbstractModifyIngredientViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "ingredients"

}
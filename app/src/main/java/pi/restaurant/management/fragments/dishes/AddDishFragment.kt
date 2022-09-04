package pi.restaurant.management.fragments.dishes

import pi.restaurant.management.R

class AddDishFragment : AbstractModifyDishFragment() {

    override val nextActionId = R.id.actionAddDishToDishes
    override val saveMessageId = R.string.dish_added
    override val removeMessageId = 0 // Unused
}
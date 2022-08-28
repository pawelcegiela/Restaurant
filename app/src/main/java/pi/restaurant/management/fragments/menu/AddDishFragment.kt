package pi.restaurant.management.fragments.menu

import pi.restaurant.management.R

class AddDishFragment : AbstractModifyDishFragment() {

    override val nextActionId = R.id.actionAddDishToMenu
    override val saveMessageId = R.string.dish_added
    override val removeMessageId = 0
}
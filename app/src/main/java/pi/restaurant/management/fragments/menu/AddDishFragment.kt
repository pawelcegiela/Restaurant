package pi.restaurant.management.fragments.menu

import pi.restaurant.management.R

class AddDishFragment : ModifyDishFragment() {

    override val saveActionId = R.id.actionAddDishToMenu
    override val toastMessageId = R.string.dish_added
}
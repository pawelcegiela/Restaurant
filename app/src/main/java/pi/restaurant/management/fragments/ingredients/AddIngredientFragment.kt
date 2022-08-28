package pi.restaurant.management.fragments.ingredients

import pi.restaurant.management.R

class AddIngredientFragment : ModifyIngredientFragment() {

    override val saveActionId = R.id.actionAddIngredientToIngredients
    override val toastMessageId = R.string.ingredient_added
}
package pi.restaurantapp.viewmodels.fragments.management.ingredients

import pi.restaurantapp.logic.fragments.management.ingredients.IngredientsMainLogic
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for IngredientsMainFragment.
 * @see pi.restaurantapp.logic.fragments.management.ingredients.IngredientsMainLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.ingredients.IngredientsMainFragment View layer
 */
class IngredientsMainViewModel : AbstractItemListViewModel() {
    override val logic = IngredientsMainLogic()
}
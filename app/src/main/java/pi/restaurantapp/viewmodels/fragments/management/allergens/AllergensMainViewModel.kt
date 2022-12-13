package pi.restaurantapp.viewmodels.fragments.management.allergens

import pi.restaurantapp.logic.fragments.management.allergens.AllergensMainLogic
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for AllergensMainFragment.
 * @see pi.restaurantapp.logic.fragments.management.allergens.AllergensMainLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.allergens.AllergensMainFragment View layer
 */
class AllergensMainViewModel : AbstractItemListViewModel() {
    override val logic = AllergensMainLogic()
}
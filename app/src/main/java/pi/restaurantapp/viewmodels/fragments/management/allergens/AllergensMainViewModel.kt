package pi.restaurantapp.viewmodels.fragments.management.allergens

import pi.restaurantapp.logic.fragments.management.allergens.AllergensMainLogic
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel

class AllergensMainViewModel : AbstractItemListViewModel() {
    override val logic = AllergensMainLogic()
}
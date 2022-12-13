package pi.restaurantapp.viewmodels.fragments.management.workers

import pi.restaurantapp.logic.fragments.management.workers.WorkersMainLogic
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for WorkersMainFragment.
 * @see pi.restaurantapp.logic.fragments.management.workers.WorkersMainLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.workers.WorkersMainFragment View layer
 */
class WorkersMainViewModel : AbstractItemListViewModel() {
    override val logic = WorkersMainLogic()
}
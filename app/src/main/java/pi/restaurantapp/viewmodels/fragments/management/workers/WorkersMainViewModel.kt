package pi.restaurantapp.viewmodels.fragments.management.workers

import pi.restaurantapp.logic.fragments.management.workers.WorkersMainLogic
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel

class WorkersMainViewModel : AbstractItemListViewModel() {
    override val logic = WorkersMainLogic()
}
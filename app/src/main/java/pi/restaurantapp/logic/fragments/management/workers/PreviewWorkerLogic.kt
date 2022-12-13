package pi.restaurantapp.logic.fragments.management.workers

import pi.restaurantapp.logic.fragments.AbstractPreviewItemLogic

/**
 * Class responsible for business logic and communication with database (Model layer) for PreviewWorkerFragment.
 * @see pi.restaurantapp.ui.fragments.management.workers.PreviewWorkerFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.workers.PreviewWorkerViewModel ViewModel layer
 */
class PreviewWorkerLogic : AbstractPreviewItemLogic() {
    override val databasePath = "users"
}
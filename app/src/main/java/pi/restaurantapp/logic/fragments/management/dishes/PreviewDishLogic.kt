package pi.restaurantapp.logic.fragments.management.dishes

import pi.restaurantapp.logic.fragments.AbstractPreviewItemLogic

/**
 * Class responsible for business logic and communication with database (Model layer) for PreviewDishFragment.
 * @see pi.restaurantapp.ui.fragments.management.dishes.PreviewDishFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.dishes.PreviewDishViewModel ViewModel layer
 */
class PreviewDishLogic : AbstractPreviewItemLogic() {
    override val databasePath = "dishes"

}
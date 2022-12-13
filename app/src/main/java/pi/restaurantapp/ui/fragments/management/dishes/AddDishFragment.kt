package pi.restaurantapp.ui.fragments.management.dishes

import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.dishes.AddDishViewModel

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for AddDishFragment.
 * @see pi.restaurantapp.viewmodels.fragments.management.dishes.AddDishViewModel ViewModel layer
 * @see pi.restaurantapp.logic.fragments.management.dishes.AddDishLogic Model layer
 */
class AddDishFragment : AbstractModifyDishFragment() {

    override val nextActionId = R.id.actionAddDishToDishes
    override val saveMessageId = R.string.dish_added
    override val removeMessageId = 0 // Unused
    override val viewModel: AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel: AddDishViewModel by viewModels()
}
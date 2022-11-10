package pi.restaurantapp.ui.fragments.management.dishes

import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.model.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.model.fragments.management.dishes.AddDishViewModel

class AddDishFragment : AbstractModifyDishFragment() {

    override val nextActionId = R.id.actionAddDishToDishes
    override val saveMessageId = R.string.dish_added
    override val removeMessageId = 0 // Unused
    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : AddDishViewModel by viewModels()
}
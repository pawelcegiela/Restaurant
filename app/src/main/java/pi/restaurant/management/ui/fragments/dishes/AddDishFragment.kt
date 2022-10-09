package pi.restaurant.management.ui.fragments.dishes

import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.model.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.model.fragments.dishes.AddDishViewModel

class AddDishFragment : AbstractModifyDishFragment() {

    override val nextActionId = R.id.actionAddDishToDishes
    override val saveMessageId = R.string.dish_added
    override val removeMessageId = 0 // Unused
    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : AddDishViewModel by viewModels()
}
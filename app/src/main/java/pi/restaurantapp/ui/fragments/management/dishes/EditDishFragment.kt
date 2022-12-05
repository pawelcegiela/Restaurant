package pi.restaurantapp.ui.fragments.management.dishes

import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.dishes.EditDishViewModel

class EditDishFragment : AbstractModifyDishFragment() {

    override val nextActionId = R.id.actionEditDishToDishes
    override val saveMessageId = R.string.dish_modified
    override val removeMessageId = R.string.dish_removed
    override val viewModel: AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel: EditDishViewModel by viewModels()

    override fun initializeUI() {
        itemId = arguments?.getString("id").toString()
    }
}
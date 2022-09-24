package pi.restaurant.management.fragments.discounts

import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.fragments.dishes.EditDishViewModel
import pi.restaurant.management.fragments.workers.EditWorkerViewModel

class EditDiscountFragment : AbstractModifyDiscountFragment() {

    override val nextActionId = R.id.actionEditDiscountToDiscounts
    override val saveMessageId = R.string.discount_modified
    override val removeMessageId = R.string.discount_removed
    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : EditDiscountViewModel by viewModels()

    //TODO: Edycja rabatów - do przemyślenia
}
package pi.restaurant.management.ui.fragments.discounts

import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.logic.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.logic.fragments.discounts.EditDiscountViewModel

class EditDiscountFragment : AbstractModifyDiscountFragment() {

    override val nextActionId = R.id.actionEditDiscountToDiscounts
    override val saveMessageId = R.string.discount_modified
    override val removeMessageId = R.string.discount_removed
    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : EditDiscountViewModel by viewModels()

    //TODO: Edycja rabatów - do przemyślenia
}
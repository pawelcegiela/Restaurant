package pi.restaurant.management.fragments.discounts

import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.fragments.workers.EditWorkerViewModel

class AddDiscountFragment : AbstractModifyDiscountFragment() {

    override val nextActionId = R.id.actionAddDiscountToDiscounts
    override val saveMessageId = R.string.discount_added
    override val removeMessageId = 0
    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : AddDiscountViewModel by viewModels()
}
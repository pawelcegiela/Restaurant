package pi.restaurantapp.ui.fragments.management.discounts

import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.model.fragments.management.AbstractModifyItemViewModel
import pi.restaurantapp.model.fragments.management.discounts.AddDiscountViewModel

class AddDiscountFragment : AbstractModifyDiscountFragment() {

    override val nextActionId = R.id.actionAddDiscountToDiscounts
    override val saveMessageId = R.string.discount_added
    override val removeMessageId = 0
    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : AddDiscountViewModel by viewModels()
}
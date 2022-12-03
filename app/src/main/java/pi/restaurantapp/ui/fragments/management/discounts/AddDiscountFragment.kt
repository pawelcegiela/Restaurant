package pi.restaurantapp.ui.fragments.management.discounts

import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.discounts.AddDiscountViewModel

class AddDiscountFragment : AbstractModifyDiscountFragment() {

    override val nextActionId = R.id.actionAddDiscountToDiscounts
    override val saveMessageId = R.string.discount_added
    override val removeMessageId = 0
    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : AddDiscountViewModel by viewModels()
}
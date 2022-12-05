package pi.restaurantapp.ui.fragments.management.discounts

import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.discounts.EditDiscountViewModel

class EditDiscountFragment : AbstractModifyDiscountFragment() {

    override val nextActionId = R.id.actionEditDiscountToDiscounts
    override val saveMessageId = R.string.discount_modified
    override val removeMessageId = R.string.discount_removed
    override val viewModel: AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel: EditDiscountViewModel by viewModels()

    override fun initializeUI() {
        itemId = arguments?.getString("id").toString()
    }
}
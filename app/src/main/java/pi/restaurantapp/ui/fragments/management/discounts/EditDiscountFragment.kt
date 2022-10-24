package pi.restaurantapp.ui.fragments.management.discounts

import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.model.fragments.management.AbstractModifyItemViewModel
import pi.restaurantapp.model.fragments.management.discounts.EditDiscountViewModel
import pi.restaurantapp.objects.data.discount.Discount
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.data.discount.DiscountDetails
import pi.restaurantapp.utils.StringFormatUtils

class EditDiscountFragment : AbstractModifyDiscountFragment() {

    override val nextActionId = R.id.actionEditDiscountToDiscounts
    override val saveMessageId = R.string.discount_modified
    override val removeMessageId = R.string.discount_removed
    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : EditDiscountViewModel by viewModels()

    override fun initializeUI() {
        itemId = arguments?.getString("id").toString()
        initializeExpirationDate()
        initializeSpinner()
    }

    override fun fillInData() {
        val data = _viewModel.item.value ?: Discount(itemId, DiscountBasic(), DiscountDetails())
        binding.editTextCode.setText(data.basic.id)
        binding.editTextAmount.setText(data.basic.amount)
        binding.spinnerType.setSelection(data.basic.type)
        binding.checkBoxThreshold.isChecked = data.basic.hasThreshold
        binding.editTextThreshold.setText(data.basic.thresholdValue)
        binding.editTextAvailable.setText(data.basic.availableDiscounts.size.toString())
        binding.textViewExpirationDate.text = StringFormatUtils.formatDateTime(data.basic.expirationDate)
        setNavigationCardsSaveRemove()
        finishLoading()
    }
}
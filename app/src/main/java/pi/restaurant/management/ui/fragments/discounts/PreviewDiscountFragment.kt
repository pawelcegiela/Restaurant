package pi.restaurant.management.ui.fragments.discounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import pi.restaurant.management.R
import pi.restaurant.management.databinding.FragmentPreviewDiscountBinding
import pi.restaurant.management.databinding.ToolbarNavigationPreviewBinding
import pi.restaurant.management.model.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.model.fragments.discounts.PreviewDiscountViewModel
import pi.restaurant.management.objects.data.discount.Discount
import pi.restaurant.management.ui.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.utils.StringFormatUtils

class PreviewDiscountFragment : AbstractPreviewItemFragment() {
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation: ToolbarNavigationPreviewBinding get() = binding.toolbarNavigation
    override val editActionId = R.id.actionPreviewDiscountToEditDiscount
    override val backActionId = R.id.actionPreviewDiscountToDiscounts
    override val viewModel: AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel: PreviewDiscountViewModel by viewModels()

    private var _binding: FragmentPreviewDiscountBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewDiscountBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun fillInData() {
        val item = _viewModel.item.value ?: Discount()

        binding.textViewDiscountValue.text =
            StringFormatUtils.formatDiscountValue(item.basic.amount, item.basic.type, requireContext())
        binding.textViewThreshold.text =
            if (item.basic.hasThreshold) StringFormatUtils.formatPrice(item.basic.thresholdValue) else getString(R.string.any)
        binding.textViewCreationDate.text = StringFormatUtils.formatDateTime(item.basic.creationDate)
        binding.textViewExpirationDate.text = StringFormatUtils.formatDateTime(item.basic.expirationDate)
        binding.textViewAvailable.text = item.basic.availableDiscounts.size.toString()
        binding.textViewAssigned.text = item.basic.assignedDiscounts.size.toString()
        binding.textViewRedeemed.text = item.basic.redeemedDiscounts.size.toString()

        viewModel.setReadyToUnlock()
    }
}
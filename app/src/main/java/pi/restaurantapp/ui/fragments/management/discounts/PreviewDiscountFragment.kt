package pi.restaurantapp.ui.fragments.management.discounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentPreviewDiscountBinding
import pi.restaurantapp.databinding.ToolbarNavigationPreviewBinding
import pi.restaurantapp.ui.dialogs.DiscountAssignCustomerDialog
import pi.restaurantapp.ui.fragments.AbstractPreviewItemFragment
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.discounts.PreviewDiscountViewModel

class PreviewDiscountFragment : AbstractPreviewItemFragment() {
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
        binding.fragment = this
        binding.lifecycleOwner = this
        return binding.root
    }

    fun onClickAddCustomer() {
        DiscountAssignCustomerDialog(this, { _viewModel.getCustomers() }, _viewModel.customers, getString(R.string.select_customer)) { customer ->
            _viewModel.addCustomer(customer) { messageId, success ->
                Toast.makeText(requireContext(), getString(messageId), Toast.LENGTH_SHORT).show()
                if (success) {
                    binding.textViewAssigned.text = _viewModel.item.value!!.basic.assignedDiscounts.size.toString()
                }
            }
        }
    }
}
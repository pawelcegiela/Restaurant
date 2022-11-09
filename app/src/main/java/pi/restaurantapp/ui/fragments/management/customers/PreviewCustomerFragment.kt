package pi.restaurantapp.ui.fragments.management.customers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentPreviewCustomerBinding
import pi.restaurantapp.databinding.ToolbarNavigationPreviewBinding
import pi.restaurantapp.model.fragments.management.AbstractPreviewItemViewModel
import pi.restaurantapp.model.fragments.management.customers.PreviewCustomerViewModel
import pi.restaurantapp.objects.data.user.User
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.ui.fragments.AbstractPreviewItemFragment
import pi.restaurantapp.utils.StringFormatUtils


class PreviewCustomerFragment : AbstractPreviewItemFragment() {
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation: ToolbarNavigationPreviewBinding get() = binding.toolbarNavigation
    override var editActionId = 0 // Warning: unused
    override val backActionId = R.id.actionPreviewCustomerToCustomers
    override val viewModel: AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel: PreviewCustomerViewModel by viewModels()

    private var _binding: FragmentPreviewCustomerBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewCustomerBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun fillInData() {
        val item = _viewModel.item.value ?: User()

        binding.textViewName.text = StringFormatUtils.format(item.basic.firstName, item.basic.lastName)
        binding.textViewRole.text = Role.getString(item.basic.role, requireContext())
        binding.textViewCreationDate.text = StringFormatUtils.formatDate(item.details.creationDate)
        binding.textViewDeliveryAddress.text = StringFormatUtils.formatAddress(item.details.defaultDeliveryAddress)
        binding.textViewContactPhone.text = item.details.contactPhone

        viewModel.setReadyToUnlock()
    }

    override fun initializeUI() {
        initializeWorkerUI()
    }
}
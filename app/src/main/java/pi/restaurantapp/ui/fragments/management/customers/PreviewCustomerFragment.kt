package pi.restaurantapp.ui.fragments.management.customers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentPreviewCustomerBinding
import pi.restaurantapp.databinding.ToolbarNavigationPreviewBinding
import pi.restaurantapp.ui.fragments.AbstractPreviewItemFragment
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.customers.PreviewCustomerViewModel

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for PreviewCustomerFragment.
 * @see pi.restaurantapp.viewmodels.fragments.management.customers.PreviewCustomerViewModel ViewModel layer
 * @see pi.restaurantapp.logic.fragments.management.customers.PreviewCustomerLogic Model layer
 */
class PreviewCustomerFragment : AbstractPreviewItemFragment() {
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
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}
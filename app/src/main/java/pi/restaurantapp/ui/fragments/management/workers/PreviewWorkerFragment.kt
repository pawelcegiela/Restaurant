package pi.restaurantapp.ui.fragments.management.workers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentPreviewWorkerBinding
import pi.restaurantapp.databinding.ToolbarNavigationPreviewBinding
import pi.restaurantapp.model.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.model.fragments.management.workers.PreviewWorkerViewModel
import pi.restaurantapp.objects.data.user.User
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.ui.fragments.AbstractPreviewItemFragment
import pi.restaurantapp.utils.StringFormatUtils

class PreviewWorkerFragment : AbstractPreviewItemFragment() {
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation: ToolbarNavigationPreviewBinding get() = binding.toolbarNavigation
    override var editActionId = R.id.actionPreviewWorkerToEditWorker
    override val backActionId = R.id.actionPreviewWorkerToWorkers
    override val viewModel: AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel: PreviewWorkerViewModel by viewModels()

    private var _binding: FragmentPreviewWorkerBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewWorkerBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun fillInData() {
        val item = _viewModel.item.value ?: User()

        binding.textViewName.text = StringFormatUtils.format(item.basic.firstName, item.basic.lastName)
        binding.textViewRole.text = Role.getString(item.basic.role, requireContext())
        binding.textViewCreationDate.text = StringFormatUtils.formatDate(item.details.creationDate)
        binding.textViewContactPhone.text = item.details.contactPhone
        binding.textViewDelivery.text = if (item.basic.delivery) getString(R.string.yes) else getString(R.string.no)

        viewModel.setReadyToUnlock()
    }

    override fun initializeUI() {
        val myRole = viewModel.userRole.value ?: Role.getPlaceholder()
        val previewedUserRole = _viewModel.item.value?.basic?.role ?: Role.getPlaceholder()
        if (myRole < previewedUserRole) {
            super.initializeUI()
        } else {
            if (Firebase.auth.uid == (_viewModel.item.value?.id ?: "")) {
                editActionId = R.id.actionPreviewWorkerToEditMyData
                super.initializeUI()
            } else {
                initializeWorkerUI()
            }
        }
    }
}
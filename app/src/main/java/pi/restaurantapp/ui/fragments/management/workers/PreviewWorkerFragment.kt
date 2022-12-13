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
import pi.restaurantapp.ui.fragments.AbstractPreviewItemFragment
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.workers.PreviewWorkerViewModel

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for PreviewWorkerFragment.
 * @see pi.restaurantapp.viewmodels.fragments.management.workers.PreviewWorkerViewModel ViewModel layer
 * @see pi.restaurantapp.logic.fragments.management.workers.PreviewWorkerLogic Model layer
 */
class PreviewWorkerFragment : AbstractPreviewItemFragment() {
    override val toolbarNavigation: ToolbarNavigationPreviewBinding get() = binding.toolbarNavigation
    override val editActionId
        get() =
            if (Firebase.auth.uid == (_viewModel.item.value?.id ?: "")) R.id.actionPreviewWorkerToEditMyData
            else R.id.actionPreviewWorkerToEditWorker
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
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}
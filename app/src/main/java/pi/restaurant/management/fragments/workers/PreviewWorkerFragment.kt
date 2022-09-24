package pi.restaurant.management.fragments.workers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.User
import pi.restaurant.management.databinding.FragmentPreviewWorkerBinding
import pi.restaurant.management.enums.Role
import pi.restaurant.management.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.utils.StringFormatUtils

class PreviewWorkerFragment : AbstractPreviewItemFragment() {
    override val linearLayout get() = binding.linearLayout
    override val editButton get() = binding.buttonEdit
    override val editActionId = R.id.actionPreviewWorkerToEditWorker
    override val viewModel : AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel : PreviewWorkerViewModel by viewModels()

    private var _binding: FragmentPreviewWorkerBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewWorkerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun fillInData(dataSnapshot: DataSnapshot) {
        val item = dataSnapshot.getValue<User>() ?: return
        binding.textViewName.text = StringFormatUtils.formatNames(item.firstName, item.lastName)
        binding.textViewEmail.text = item.email
        binding.textViewRole.text = Role.getString(item.role, requireContext())
        binding.progress.progressBar.visibility = View.GONE
    }

    // TODO Temporary - somehow check preconditions first!
    override fun initializeUI() {}
}
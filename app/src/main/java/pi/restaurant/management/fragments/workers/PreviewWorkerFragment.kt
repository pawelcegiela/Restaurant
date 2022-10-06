package pi.restaurant.management.fragments.workers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.*
import pi.restaurant.management.databinding.FragmentPreviewWorkerBinding
import pi.restaurant.management.enums.Role
import pi.restaurant.management.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.utils.SnapshotsPair
import pi.restaurant.management.utils.StringFormatUtils

class PreviewWorkerFragment : AbstractPreviewItemFragment() {
    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val cardSetNavigation get() = binding.cardSetNavigation
    override val editActionId = R.id.actionPreviewWorkerToEditWorker
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
        return binding.root
    }

    private fun getItem(snapshotsPair: SnapshotsPair): User {
        val basic = snapshotsPair.basic?.getValue<UserBasic>() ?: UserBasic()
        val details = snapshotsPair.details?.getValue<UserDetails>() ?: UserDetails()
        return User(itemId, basic, details)
    }

    override fun fillInData(snapshotsPair: SnapshotsPair) {
        val item = getItem(snapshotsPair)

        binding.textViewName.text = StringFormatUtils.formatNames(item.basic.firstName, item.basic.lastName)
        binding.textViewEmail.text = item.details.email
        binding.textViewRole.text = Role.getString(item.basic.role, requireContext())
        binding.textViewCreationDate.text = StringFormatUtils.formatDate(item.details.creationDate)
        binding.textViewDelivery.text = if (item.basic.delivery) getString(R.string.yes) else getString(R.string.no)

        viewModel.liveReadyToUnlock.value = true
    }

    override fun initializeUI() {
        val myRole = viewModel.liveUserRole.value ?: Role.getPlaceholder()
        val previewedUserRole = viewModel.liveDataSnapshot.value?.basic?.getValue<UserBasic>()?.role ?: Role.getPlaceholder()
        if (myRole < previewedUserRole) {
            super.initializeUI()
        } else {
            initializeWorkerUI()
        }
    }
}
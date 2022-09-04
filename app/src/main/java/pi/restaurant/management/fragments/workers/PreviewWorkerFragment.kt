package pi.restaurant.management.fragments.workers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.User
import pi.restaurant.management.databinding.FragmentPreviewWorkerBinding
import pi.restaurant.management.enums.Role
import pi.restaurant.management.fragments.AbstractPreviewItemFragment

class PreviewWorkerFragment : AbstractPreviewItemFragment() {
    override val databasePath = "users"
    override val linearLayout get() = binding.linearLayout
    override val editButton get() = binding.buttonEdit
    override val editActionId = R.id.actionPreviewWorkerToEditWorker

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
        binding.textViewName.text = "${item.firstName} ${item.lastName}"
        binding.textViewEmail.text = item.email
        binding.textViewRole.text = getString(Role.getNameResById(item.role))
        binding.progress.progressBar.visibility = View.GONE
    }

    // TODO Temporary - somehow check preconditions first!
    override fun unlockUI() {}
}
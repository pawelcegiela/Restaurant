package pi.restaurant.management.fragments.allergens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.Allergen
import pi.restaurant.management.databinding.FragmentPreviewAllergenBinding
import pi.restaurant.management.fragments.AbstractPreviewItemFragment
import pi.restaurant.management.fragments.AbstractPreviewItemViewModel

class PreviewAllergenFragment : AbstractPreviewItemFragment() {
    override val linearLayout get() = binding.linearLayout
    override val editButton get() = binding.buttonEdit
    override val editActionId = R.id.actionPreviewAllergenToEditAllergen
    override val viewModel : AbstractPreviewItemViewModel get() = _viewModel
    private val _viewModel : PreviewAllergenViewModel by viewModels()

    private var _binding: FragmentPreviewAllergenBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewAllergenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun fillInData(dataSnapshot: DataSnapshot) {
        val item = dataSnapshot.getValue<Allergen>() ?: return
        binding.textViewName.text = item.name
        binding.progress.progressBar.visibility = View.GONE
    }
}
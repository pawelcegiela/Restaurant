package pi.restaurantapp.ui.fragments.management.allergens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentModifyAllergenBinding
import pi.restaurantapp.ui.fragments.AbstractModifyItemFragment
import pi.restaurantapp.viewmodels.fragments.management.allergens.AbstractModifyAllergenViewModel

/**
 * Abstract class responsible for direct communication and displaying information to the user (View layer) for AbstractModifyAllergenFragment.
 * @see pi.restaurantapp.viewmodels.fragments.management.allergens.AbstractModifyAllergenViewModel ViewModel layer
 * @see pi.restaurantapp.logic.fragments.management.allergens.AbstractModifyAllergenLogic Model layer
 */
abstract class AbstractModifyAllergenFragment : AbstractModifyItemFragment() {

    private var _binding: FragmentModifyAllergenBinding? = null
    val binding get() = _binding!!

    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation get() = binding.toolbarNavigation
    override var itemId = ""
    private val _viewModel get() = viewModel as AbstractModifyAllergenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyAllergenBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextName] = R.string.name
        return map
    }
}
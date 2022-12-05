package pi.restaurantapp.ui.fragments.management.restaurantdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentModifyAboutRestaurantBinding
import pi.restaurantapp.ui.fragments.AbstractModifyItemFragment
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.restaurantdata.EditAboutRestaurantViewModel

class EditAboutRestaurantFragment : AbstractModifyItemFragment() {
    private var _binding: FragmentModifyAboutRestaurantBinding? = null
    private val binding get() = _binding!!

    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation get() = binding.toolbarNavigation
    override var itemId = "aboutRestaurant"
    override val nextActionId = R.id.actionAboutRestaurantToRD
    override val saveMessageId = R.string.restaurant_data_modified
    override val removeMessageId = 0 // Unused

    override val viewModel: AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel: EditAboutRestaurantViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyAboutRestaurantBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun initializeUI() {}

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextRestaurantName] = R.string.restaurant_name
        map[binding.editTextRestaurantDescription] = R.string.restaurant_description
        return map
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
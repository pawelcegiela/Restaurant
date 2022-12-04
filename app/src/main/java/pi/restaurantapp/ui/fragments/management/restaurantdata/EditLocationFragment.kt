package pi.restaurantapp.ui.fragments.management.restaurantdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentModifyLocationBinding
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.address.AddressDetails
import pi.restaurantapp.ui.fragments.AbstractModifyItemFragment
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.restaurantdata.EditLocationViewModel

class EditLocationFragment : AbstractModifyItemFragment() {
    private var _binding: FragmentModifyLocationBinding? = null
    private val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation get() = binding.toolbarNavigation
    override var itemId = "location"
    override val nextActionId = R.id.actionLocationToRD
    override val saveMessageId = R.string.location_modified
    override val removeMessageId = 0 // Unused

    override val viewModel: AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel: EditLocationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyLocationBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.fragment = this
        binding.lifecycleOwner = this
        binding.linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun initializeUI() {
        setNavigationCardsSave()
    }

    override fun fillInData() {}

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.address.editTextStreet] = R.string.street
        map[binding.address.editTextHouseNumber] = R.string.house_number
        map[binding.address.editTextPostalCode] = R.string.postal_code
        map[binding.address.editTextCity] = R.string.city
        return map
    }

    override fun getDataObject(): SplitDataObject {
        return SplitDataObject(itemId, _viewModel.item.value!!.basic, AddressDetails())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
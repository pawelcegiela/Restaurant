package pi.restaurantapp.ui.fragments.management.restaurantdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentModifyLocationBinding
import pi.restaurantapp.model.fragments.management.AbstractModifyItemViewModel
import pi.restaurantapp.model.fragments.management.restaurantdata.EditLocationViewModel
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.address.Address
import pi.restaurantapp.objects.data.address.AddressBasic
import pi.restaurantapp.objects.data.address.AddressDetails
import pi.restaurantapp.ui.fragments.management.AbstractModifyItemFragment
import pi.restaurantapp.utils.StringFormatUtils

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
        binding.linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun initializeUI() {
        setNavigationCardsSave()
    }

    override fun fillInData() {
        val data = _viewModel.item.value ?: Address()
        binding.address.editTextCity.setText(data.basic.city)
        binding.address.editTextPostalCode.setText(data.basic.postalCode)
        binding.address.editTextStreet.setText(data.basic.street)
        binding.address.editTextHouseNumber.setText(data.basic.houseNumber)
        binding.address.editTextFlatNumber.setText(data.basic.flatNumber)
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.address.editTextStreet] = R.string.street
        map[binding.address.editTextHouseNumber] = R.string.house_number
        map[binding.address.editTextPostalCode] = R.string.postal_code
        map[binding.address.editTextCity] = R.string.city
        return map
    }

    override fun getDataObject(): SplitDataObject {
        itemId = itemId.ifEmpty { StringFormatUtils.formatId() }

        val basic = AddressBasic(
            city = binding.address.editTextCity.text.toString(),
            postalCode = binding.address.editTextPostalCode.text.toString(),
            street = binding.address.editTextStreet.text.toString(),
            houseNumber = binding.address.editTextHouseNumber.text.toString(),
            flatNumber = binding.address.editTextFlatNumber.text.toString()
        )

        return SplitDataObject(itemId, basic, AddressDetails())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
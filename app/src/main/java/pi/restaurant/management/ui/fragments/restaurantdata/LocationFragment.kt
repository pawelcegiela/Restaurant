package pi.restaurant.management.ui.fragments.restaurantdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.objects.data.*
import pi.restaurant.management.objects.data.address.Address
import pi.restaurant.management.objects.data.address.AddressBasic
import pi.restaurant.management.objects.data.address.AddressDetails
import pi.restaurant.management.databinding.FragmentLocationBinding
import pi.restaurant.management.ui.fragments.AbstractModifyItemFragment
import pi.restaurant.management.logic.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.logic.fragments.restaurantdata.LocationViewModel
import pi.restaurant.management.objects.SnapshotsPair
import pi.restaurant.management.utils.StringFormatUtils

class LocationFragment : AbstractModifyItemFragment() {
    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val cardSetNavigation get() = binding.cardSetNavigation
    override var itemId = "location"
    override val nextActionId = R.id.actionLocationToRD
    override val saveMessageId = R.string.location_modified
    override val removeMessageId = 0 // Unused

    override val viewModel: AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel: LocationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        binding.linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun initializeUI() {
        setNavigationCardsSave()
    }

    private fun getItem(snapshotsPair: SnapshotsPair): Address {
        val basic = snapshotsPair.basic?.getValue<AddressBasic>() ?: AddressBasic()
        val details = snapshotsPair.details?.getValue<AddressDetails>() ?: AddressDetails()
        return Address(basic, details)
    }

    override fun fillInData(snapshotsPair: SnapshotsPair) {
        val data = getItem(snapshotsPair)
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
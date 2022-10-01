package pi.restaurant.management.fragments.restaurantdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.*
import pi.restaurant.management.databinding.FragmentLocationBinding
import pi.restaurant.management.fragments.AbstractModifyItemFragment
import pi.restaurant.management.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.utils.SnapshotsPair
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

    private fun getItem(snapshotsPair: SnapshotsPair): Location {
        val basic = snapshotsPair.basic?.getValue<LocationBasic>() ?: LocationBasic()
        val details = snapshotsPair.details?.getValue<LocationDetails>() ?: LocationDetails()
        return Location(basic, details)
    }

    override fun fillInData(snapshotsPair: SnapshotsPair) {
        val data = getItem(snapshotsPair)
        binding.editTextCity.setText(data.basic.city)
        binding.editTextPostalCode.setText(data.basic.postalCode)
        binding.editTextStreet.setText(data.basic.street)
        binding.editTextHouseNumber.setText(data.basic.houseNumber)
        binding.editTextFlatNumber.setText(data.basic.flatNumber)
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextStreet] = R.string.street
        map[binding.editTextHouseNumber] = R.string.house_number
        map[binding.editTextPostalCode] = R.string.postal_code
        map[binding.editTextCity] = R.string.city
        return map
    }

    override fun getDataObject(): SplitDataObject {
        itemId = itemId.ifEmpty { StringFormatUtils.formatId() }

        val basic = LocationBasic(
            city = binding.editTextCity.text.toString(),
            postalCode = binding.editTextPostalCode.text.toString(),
            street = binding.editTextStreet.text.toString(),
            houseNumber = binding.editTextHouseNumber.text.toString(),
            flatNumber = binding.editTextFlatNumber.text.toString()
        )

        return SplitDataObject(itemId, basic, LocationDetails())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
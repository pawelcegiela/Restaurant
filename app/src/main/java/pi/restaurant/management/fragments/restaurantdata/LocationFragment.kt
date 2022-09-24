package pi.restaurant.management.fragments.restaurantdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.Location
import pi.restaurant.management.databinding.FragmentLocationBinding
import pi.restaurant.management.fragments.AbstractModifyItemFragment
import pi.restaurant.management.fragments.AbstractModifyItemViewModel

class LocationFragment : AbstractModifyItemFragment() {
    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val saveButton get() = binding.buttonSave
    override val removeButton: Button? = null
    override var itemId = "location"
    override val nextActionId = R.id.actionLocationToRD
    override val saveMessageId = R.string.location_modified
    override val removeMessageId = 0 // Unused

    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : LocationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        binding.linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun initializeUI() {
        setSaveButtonListener()
    }

    override fun fillInData(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<Location>() ?: return
        binding.editTextCity.setText(data.city)
        binding.editTextPostalCode.setText(data.postalCode)
        binding.editTextStreet.setText(data.street)
        binding.editTextHouseNumber.setText(data.houseNumber)
        binding.editTextFlatNumber.setText(data.flatNumber)
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextStreet] = R.string.street
        map[binding.editTextHouseNumber] = R.string.house_number
        map[binding.editTextPostalCode] = R.string.postal_code
        map[binding.editTextCity] = R.string.city
        return map
    }

    override fun getDataObject(): Location {
        val location = Location()

        location.city = binding.editTextCity.text.toString()
        location.postalCode = binding.editTextPostalCode.text.toString()
        location.street = binding.editTextStreet.text.toString()
        location.houseNumber = binding.editTextHouseNumber.text.toString()
        location.flatNumber = binding.editTextFlatNumber.text.toString()

        return location
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
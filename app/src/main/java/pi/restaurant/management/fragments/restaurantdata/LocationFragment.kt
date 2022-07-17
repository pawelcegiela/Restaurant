package pi.restaurant.management.fragments.restaurantdata

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.data.Location
import pi.restaurant.management.databinding.FragmentLocationBinding

class LocationFragment : Fragment() {
    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()
        setButtonListener()
    }

    private fun loadData() {
        val databaseRef = Firebase.database.getReference("restaurantData").child("location")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue<Location>() ?: return

                setLocation(data)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("MyData", "Failed to read value.", error.toException())
            }
        })
    }

    private fun setLocation(data: Location) {
        binding.editTextCity.setText(data.city)
        binding.editTextPostalCode.setText(data.postalCode)
        binding.editTextStreet.setText(data.street)
        binding.editTextHouseNumber.setText(data.houseNumber)
        binding.editTextFlatNumber.setText(data.flatNumber)
    }

    private fun setButtonListener() {
        val databaseRef = Firebase.database.getReference("restaurantData").child("location")
        binding.buttonSave.setOnClickListener {
            databaseRef.setValue(getData())
        }
    }

    private fun getData(): Location {
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
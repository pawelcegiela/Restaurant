package pi.restaurant.management.fragments.restaurantdata

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.data.AboutRestaurant
import pi.restaurant.management.databinding.FragmentAboutRestaurantBinding
import pi.restaurant.management.fragments.SplashScreenFragment
import java.text.ParseException

class AboutRestaurantFragment : SplashScreenFragment() {
    private var _binding: FragmentAboutRestaurantBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutRestaurantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()
        setButtonListener()
    }

    private fun loadData() {
        val databaseRef = Firebase.database.getReference("restaurantData").child("aboutRestaurant")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue<AboutRestaurant>() ?: return
                binding.editTextRestaurantName.setText(data.name)
                binding.editTextRestaurantDescription.setText(data.description)
                keepSplashScreen = false
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("MyData", "Failed to read value.", error.toException())
            }
        })
    }

    private fun setButtonListener() {
        val databaseRef = Firebase.database.getReference("restaurantData").child("aboutRestaurant")
        binding.buttonSave.setOnClickListener {
            try {
                databaseRef.setValue(getData())
                Toast.makeText(
                    activity,
                    getString(R.string.restaurant_data_changed),
                    Toast.LENGTH_SHORT
                ).show()
            } catch (ex: ParseException) {
                Toast.makeText(
                    activity,
                    getString(R.string.enter_correct_hours),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getData(): AboutRestaurant {
        val data = AboutRestaurant()

        data.name = binding.editTextRestaurantName.text.toString()
        data.description = binding.editTextRestaurantDescription.text.toString()

        return data
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
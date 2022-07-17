package pi.restaurant.management.fragments.restaurantdata

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.data.OpeningHours
import pi.restaurant.management.databinding.FragmentOpeningHoursBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class OpeningHoursFragment : Fragment() {
    private var _binding: FragmentOpeningHoursBinding? = null
    private val binding get() = _binding!!

    private lateinit var checkBoxes: ArrayList<CheckBox>
    private lateinit var etOpenings: ArrayList<EditText>
    private lateinit var etClosings: ArrayList<EditText>

    private lateinit var daysEnabled: ArrayList<Boolean>
    private lateinit var daysOpeningValues: ArrayList<Date>
    private lateinit var daysClosingValues: ArrayList<Date>

    private val sdf = SimpleDateFormat("HH:mm")
    private lateinit var defaultOpening: Date
    private lateinit var defaultClosing: Date

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpeningHoursBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeArrays()
        loadData()
        setCheckBoxListeners()
        setButtonListener()
    }

    private fun initializeArrays() {
        checkBoxes = arrayListOf(
            binding.checkBoxMonday,
            binding.checkBoxTuesday,
            binding.checkBoxWednesday,
            binding.checkBoxThursday,
            binding.checkBoxFriday,
            binding.checkBoxSaturday,
            binding.checkBoxSunday
        )

        etOpenings = arrayListOf(
            binding.editTextMondayStartHour,
            binding.editTextTuesdayStartHour,
            binding.editTextWednesdayStartHour,
            binding.editTextThursdayStartHour,
            binding.editTextFridayStartHour,
            binding.editTextSaturdayStartHour,
            binding.editTextSundayStartHour
        )

        etClosings = arrayListOf(
            binding.editTextMondayEndHour,
            binding.editTextTuesdayEndHour,
            binding.editTextWednesdayEndHour,
            binding.editTextThursdayEndHour,
            binding.editTextFridayEndHour,
            binding.editTextSaturdayEndHour,
            binding.editTextSundayEndHour
        )
    }

    private fun loadData() {
        val databaseRef = Firebase.database.getReference("restaurantData").child("openingHours")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue<OpeningHours>() ?: return
                setOpeningHours(data)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("MyData", "Failed to read value.", error.toException())
            }
        })
    }

    private fun setOpeningHours(openingHours: OpeningHours) {
        defaultOpening = openingHours.defaultStartHour as Date
        defaultClosing = openingHours.defaultEndHour as Date

        binding.editTextDefaultStartHour.setText(sdf.format(defaultOpening))
        binding.editTextDefaultEndHour.setText(sdf.format(defaultClosing))

        daysEnabled = openingHours.getWeekDaysEnabled()
        daysOpeningValues = openingHours.getWeekDaysStartHour()
        daysClosingValues = openingHours.getWeekDaysEndHour()

        for (i in checkBoxes.indices) {
            val enabled = daysEnabled[i]
            checkBoxes[i].isChecked = enabled
            etOpenings[i].isEnabled = enabled
            etClosings[i].isEnabled = enabled

            if (enabled) {
                etOpenings[i].setText(sdf.format(daysOpeningValues[i]))
                etClosings[i].setText(sdf.format(daysClosingValues[i]))
            } else {
                etOpenings[i].setText("-")
                etClosings[i].setText("-")
            }
        }
    }

    private fun setCheckBoxListeners() {
        for (i in checkBoxes.indices) {
            checkBoxes[i].setOnCheckedChangeListener { _, isChecked ->
                etOpenings[i].isEnabled = isChecked
                etClosings[i].isEnabled = isChecked

                if (isChecked) {
                    etOpenings[i].setText(sdf.format(defaultOpening))
                    etClosings[i].setText(sdf.format(defaultClosing))
                } else {
                    etOpenings[i].setText("-")
                    etClosings[i].setText("-")
                }
            }
        }
    }

    private fun setButtonListener() {
        val databaseRef = Firebase.database.getReference("restaurantData").child("openingHours")
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

    private fun getData(): OpeningHours {
        val openingHours = OpeningHours()

        openingHours.defaultStartHour = sdf.parse(binding.editTextDefaultStartHour.text.toString())
        openingHours.defaultEndHour = sdf.parse(binding.editTextDefaultEndHour.text.toString())

        for (i in daysEnabled.indices) {
            daysEnabled[i] = checkBoxes[i].isChecked
            if (etOpenings[i].text.toString() == "-") {
                daysOpeningValues[i] = defaultOpening
                daysClosingValues[i] = defaultClosing
            } else {
                daysOpeningValues[i] =
                    sdf.parse(etOpenings[i].text.toString()) as Date
                daysClosingValues[i] = sdf.parse(etClosings[i].text.toString()) as Date
            }
        }
        openingHours.setWeekDaysEnabled(daysEnabled)
        openingHours.setWeekDaysStartHour(daysOpeningValues)
        openingHours.setWeekDaysEndHour(daysClosingValues)

        return openingHours
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
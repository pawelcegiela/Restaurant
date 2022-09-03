package pi.restaurant.management.fragments.restaurantdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.OpeningHours
import pi.restaurant.management.databinding.FragmentOpeningHoursBinding
import pi.restaurant.management.enums.Precondition
import pi.restaurant.management.fragments.AbstractModifyItemFragment
import pi.restaurant.management.utils.PreconditionUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class OpeningHoursFragment : AbstractModifyItemFragment() {
    private var _binding: FragmentOpeningHoursBinding? = null
    private val binding get() = _binding!!

    override val databasePath = "restaurantData"
    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val saveButton get() = binding.buttonSave
    override val removeButton: Button? = null
    override var itemId = "openingHours"
    override val nextActionId = R.id.actionOpeningHoursToRD
    override val saveMessageId = R.string.opening_hours_modified
    override val removeMessageId = 0 // Unused

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
        binding.linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun initializeUI() {
        initializeArrays()
        getDataFromDatabase()
        setCheckBoxListeners()
        setSaveButtonListener()
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

    override fun fillInData(dataSnapshot: DataSnapshot) {
        val openingHours = dataSnapshot.getValue<OpeningHours>() ?: OpeningHours()

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

    override fun getEditTextMap(): Map<EditText, Int> {
        return HashMap()
    }

    private fun setCheckBoxListeners() {
        for (i in checkBoxes.indices) {
            checkBoxes[i].setOnCheckedChangeListener { _, isChecked ->
                changeDay(i, isChecked)
            }
            changeDay(i, checkBoxes[i].isChecked)
        }
    }

    private fun changeDay(i: Int, isChecked: Boolean) {
        etOpenings[i].isEnabled = isChecked
        etClosings[i].isEnabled = isChecked

        if (isChecked) {
            etOpenings[i].setText(binding.editTextDefaultStartHour.text.toString())
            etClosings[i].setText(binding.editTextDefaultEndHour.text.toString())
        } else {
            etOpenings[i].setText("-")
            etClosings[i].setText("-")
        }
    }

    override fun checkSavePreconditions(data: AbstractDataObject): Precondition {
        if (super.checkSavePreconditions(data) != Precondition.OK) {
            return super.checkSavePreconditions(data)
        }
        return PreconditionUtils.checkOpeningHoursError(data as OpeningHours)
    }

    override fun getDataObject(): OpeningHours {
        val openingHours = OpeningHours()

        try {
            openingHours.defaultStartHour =
                sdf.parse(binding.editTextDefaultStartHour.text.toString())
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
        } catch (ex: ParseException) {
            openingHours.isError = true
        }

        return openingHours
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
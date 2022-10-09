package pi.restaurant.management.ui.fragments.restaurantdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.objects.data.*
import pi.restaurant.management.objects.data.openinghours.OpeningHours
import pi.restaurant.management.objects.data.openinghours.OpeningHoursBasic
import pi.restaurant.management.objects.data.openinghours.OpeningHoursDetails
import pi.restaurant.management.databinding.FragmentOpeningHoursBinding
import pi.restaurant.management.objects.enums.Precondition
import pi.restaurant.management.ui.fragments.AbstractModifyItemFragment
import pi.restaurant.management.model.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.model.fragments.restaurantdata.OpeningHoursViewModel
import pi.restaurant.management.utils.PreconditionUtils
import pi.restaurant.management.objects.SnapshotsPair
import pi.restaurant.management.utils.StringFormatUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class OpeningHoursFragment : AbstractModifyItemFragment() {
    private var _binding: FragmentOpeningHoursBinding? = null
    private val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val cardSetNavigation get() = binding.cardSetNavigation
    override var itemId = "openingHours"
    override val nextActionId = R.id.actionOpeningHoursToRD
    override val saveMessageId = R.string.opening_hours_modified
    override val removeMessageId = 0 // Unused

    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : OpeningHoursViewModel by viewModels()

    private lateinit var checkBoxes: ArrayList<CheckBox>
    private lateinit var etOpenings: ArrayList<EditText>
    private lateinit var etClosings: ArrayList<EditText>

    private lateinit var daysEnabled: ArrayList<Boolean>
    private lateinit var daysOpeningValues: ArrayList<Date>
    private lateinit var daysClosingValues: ArrayList<Date>

    private val sdf = SimpleDateFormat("HH:mm", Locale.ROOT)
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
        setCheckBoxListeners()
        setNavigationCardsSave()
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

    override fun fillInData() {
        val openingHours = _viewModel.item.value ?: OpeningHours()

        defaultOpening = openingHours.basic.defaultStartHour as Date
        defaultClosing = openingHours.basic.defaultEndHour as Date

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
        return PreconditionUtils.checkOpeningHoursError(data as OpeningHoursBasic)
    }

    override fun getDataObject(): SplitDataObject {
        itemId = itemId.ifEmpty { StringFormatUtils.formatId() }
        val basic = OpeningHoursBasic()

        try {
            basic.defaultStartHour =
                sdf.parse(binding.editTextDefaultStartHour.text.toString())
            basic.defaultEndHour = sdf.parse(binding.editTextDefaultEndHour.text.toString())

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
            basic.setWeekDaysEnabled(daysEnabled)
            basic.setWeekDaysStartHour(daysOpeningValues)
            basic.setWeekDaysEndHour(daysClosingValues)
        } catch (ex: ParseException) {
            basic.isError = true
        }

        return SplitDataObject(itemId, basic, OpeningHoursDetails())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
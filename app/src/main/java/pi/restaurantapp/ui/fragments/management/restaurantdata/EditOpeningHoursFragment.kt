package pi.restaurantapp.ui.fragments.management.restaurantdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentModifyOpeningHoursBinding
import pi.restaurantapp.model.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.model.fragments.management.restaurantdata.EditOpeningHoursViewModel
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.openinghours.OpeningHours
import pi.restaurantapp.objects.data.openinghours.OpeningHoursBasic
import pi.restaurantapp.objects.data.openinghours.OpeningHoursDetails
import pi.restaurantapp.objects.enums.Precondition
import pi.restaurantapp.ui.fragments.AbstractModifyItemFragment
import pi.restaurantapp.ui.pickers.TimePickerFragment
import pi.restaurantapp.utils.ComputingUtils
import pi.restaurantapp.utils.PreconditionUtils
import pi.restaurantapp.utils.StringFormatUtils
import java.text.SimpleDateFormat
import java.util.*

class EditOpeningHoursFragment : AbstractModifyItemFragment() {
    private var _binding: FragmentModifyOpeningHoursBinding? = null
    private val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation get() = binding.toolbarNavigation
    override var itemId = "openingHours"
    override val nextActionId = R.id.actionOpeningHoursToRD
    override val saveMessageId = R.string.opening_hours_modified
    override val removeMessageId = 0 // Unused

    override val viewModel: AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel: EditOpeningHoursViewModel by viewModels()

    private lateinit var daysEnabled: ArrayList<Boolean>
    private lateinit var daysOpeningValues: ArrayList<Date>
    private lateinit var daysClosingValues: ArrayList<Date>

    private val sdf = SimpleDateFormat("HH:mm", Locale.ROOT)
    private lateinit var defaultOpening: Date
    private lateinit var defaultClosing: Date

    private val daySets
        get() = arrayListOf(
            binding.setMonday,
            binding.setTuesday,
            binding.setWednesday,
            binding.setThursday,
            binding.setFriday,
            binding.setSaturday,
            binding.setSunday,
            binding.setDefault
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyOpeningHoursBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.fragment = this
        binding.lifecycleOwner = this
        binding.linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun initializeUI() {
        setNavigationCardsSave()

        binding.setDefault.checkBox.isChecked = true
        binding.setDefault.checkBox.visibility = View.GONE

        initializeDaySets()
    }

    private fun initializeDaySets() {
        for (set in daySets) {
            set.checkBox.setOnClickListener {
                if (set.checkBox.isChecked) {
                    set.textViewStartHour.text = binding.setDefault.textViewStartHour.text
                    set.textViewEndHour.text = binding.setDefault.textViewEndHour.text
                    set.textViewDash.text = getString(R.string.dash)
                } else {
                    set.textViewStartHour.text = ""
                    set.textViewEndHour.text = ""
                    set.textViewDash.text = ""
                }
            }

            set.textViewStartHour.setOnClickListener {
                if (set.checkBox.isChecked) {
                    TimePickerFragment(ComputingUtils.getTimeFromString(set.textViewStartHour.text.toString())) { time ->
                        set.textViewStartHour.text = time
                    }.show(requireActivity().supportFragmentManager, "timePicker")
                }
            }

            set.textViewEndHour.setOnClickListener {
                if (set.checkBox.isChecked) {
                    TimePickerFragment(ComputingUtils.getTimeFromString(set.textViewEndHour.text.toString())) { time ->
                        set.textViewEndHour.text = time
                    }.show(requireActivity().supportFragmentManager, "timePicker")
                }
            }
        }
    }

    override fun fillInData() {
        val openingHours = _viewModel.item.value ?: OpeningHours()

        defaultOpening = openingHours.basic.defaultStartHour
        defaultClosing = openingHours.basic.defaultEndHour

        binding.setDefault.textViewStartHour.text = StringFormatUtils.formatTime(defaultOpening)
        binding.setDefault.textViewEndHour.text = StringFormatUtils.formatTime(defaultClosing)

        daysEnabled = openingHours.basic.getWeekDaysEnabled()
        daysOpeningValues = openingHours.basic.getWeekDaysStartHour()
        daysClosingValues = openingHours.basic.getWeekDaysEndHour()

        for (i in daysEnabled.indices) {
            val enabled = daysEnabled[i]
            daySets[i].checkBox.isChecked = enabled

            if (enabled) {
                daySets[i].textViewStartHour.text = StringFormatUtils.formatTime(daysOpeningValues[i])
                daySets[i].textViewEndHour.text = StringFormatUtils.formatTime(daysClosingValues[i])
                daySets[i].textViewDash.text = getString(R.string.dash)
            } else {
                daySets[i].textViewStartHour.text = ""
                daySets[i].textViewEndHour.text = ""
                daySets[i].textViewDash.text = ""
            }
        }
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        return HashMap()
    }

    override fun checkSavePreconditions(data: SplitDataObject): Precondition {
        if (super.checkSavePreconditions(data) != Precondition.OK) {
            return super.checkSavePreconditions(data)
        }
        return PreconditionUtils.checkOpeningHoursError(data.basic as OpeningHoursBasic)
    }

    override fun getDataObject(): SplitDataObject {
        itemId = itemId.ifEmpty { StringFormatUtils.formatId() }
        val basic = OpeningHoursBasic()

        basic.defaultStartHour = ComputingUtils.getTimeFromString(binding.setDefault.textViewStartHour.text.toString())
        basic.defaultEndHour = ComputingUtils.getTimeFromString(binding.setDefault.textViewEndHour.text.toString())

        for (i in daysEnabled.indices) {
            daysEnabled[i] = daySets[i].checkBox.isChecked
            if (daySets[i].checkBox.isChecked) {
                daysOpeningValues[i] =
                    sdf.parse(daySets[i].textViewStartHour.text.toString()) as Date
                daysClosingValues[i] = sdf.parse(daySets[i].textViewEndHour.text.toString()) as Date
            } else {
                daysOpeningValues[i] = defaultOpening
                daysClosingValues[i] = defaultClosing
            }
        }
        basic.setWeekDaysEnabled(daysEnabled)
        basic.setWeekDaysStartHour(daysOpeningValues)
        basic.setWeekDaysEndHour(daysClosingValues)
        basic.isError = checkValues()

        return SplitDataObject(itemId, basic, OpeningHoursDetails())
    }

    private fun checkValues() : Boolean {
        for (i in daysEnabled.indices) {
            if (daysOpeningValues[i] >= daysClosingValues[i]) {
                return true
            }
        }
        return (defaultOpening >= defaultClosing)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
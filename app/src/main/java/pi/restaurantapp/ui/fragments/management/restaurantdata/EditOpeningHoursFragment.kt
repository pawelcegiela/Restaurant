package pi.restaurantapp.ui.fragments.management.restaurantdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentModifyOpeningHoursBinding
import pi.restaurantapp.logic.utils.ComputingUtils
import pi.restaurantapp.ui.fragments.AbstractModifyItemFragment
import pi.restaurantapp.ui.pickers.TimePickerFragment
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.restaurantdata.EditOpeningHoursViewModel

class EditOpeningHoursFragment : AbstractModifyItemFragment() {
    private var _binding: FragmentModifyOpeningHoursBinding? = null
    private val binding get() = _binding!!

    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation get() = binding.toolbarNavigation
    override var itemId = "openingHours"
    override val nextActionId = R.id.actionOpeningHoursToRD
    override val saveMessageId = R.string.opening_hours_modified
    override val removeMessageId = 0 // Unused

    override val viewModel: AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel: EditOpeningHoursViewModel by viewModels()

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
        binding.lifecycleOwner = viewLifecycleOwner
        binding.linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun initializeUI() {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
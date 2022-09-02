package pi.restaurant.management.fragments.discounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import pi.restaurant.management.R
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.DiscountGroup
import pi.restaurant.management.databinding.FragmentModifyDiscountBinding
import pi.restaurant.management.fragments.AbstractModifyItemFragment
import pi.restaurant.management.utils.Utils
import java.util.*

abstract class AbstractModifyDiscountFragment : AbstractModifyItemFragment() {

    private var _binding: FragmentModifyDiscountBinding? = null
    val binding get() = _binding!!

    override val databasePath = "discounts"
    override val linearLayout get() = binding.linearLayout
    override val saveButton get() = binding.buttonSave
    override val removeButton get() = binding.buttonRemove
    override var itemId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyDiscountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initializeUI() {
        binding.progress.progressBar.visibility = View.GONE
        initializeSpinner()
        setSaveButtonListener()
    }

    private fun initializeSpinner() {
        val spinner: Spinner = binding.spinnerType
        ArrayAdapter.createFromResource(
            context!!,
            R.array.types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    override fun getDataObject(): AbstractDataObject {
        val availableNumber = binding.editTextAvailable.text.toString().toInt()
        val assignedNumber = binding.editTextAssigned.text.toString().toInt()
        val usedNumber = binding.editTextUsed.text.toString().toInt()
        val code = binding.editTextCode.text.toString()
        val type = binding.spinnerType.selectedItemId.toInt()
        val amount = binding.editTextAmount.text.toString().toDouble()
        val date = Date() //TODO

        val available = Utils.createDiscounts(code, availableNumber, 0)
        val assigned = Utils.createDiscounts(code, assignedNumber, availableNumber)
        val used = Utils.createDiscounts(code, usedNumber, availableNumber + assignedNumber)

        return DiscountGroup(available, assigned, used, code, type, amount, date)
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextAvailable] = R.string.number_of_available_discounts
        map[binding.editTextAssigned] = R.string.number_of_assigned_discounts
        map[binding.editTextUsed] = R.string.number_of_used_discounts
        map[binding.editTextCode] = R.string.code
        map[binding.editTextAmount] = R.string.amount
        return map
    }
}
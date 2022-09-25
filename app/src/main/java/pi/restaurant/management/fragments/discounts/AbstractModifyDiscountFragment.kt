package pi.restaurant.management.fragments.discounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import pi.restaurant.management.R
import pi.restaurant.management.data.*
import pi.restaurant.management.databinding.FragmentModifyDiscountBinding
import pi.restaurant.management.enums.DiscountType
import pi.restaurant.management.fragments.AbstractModifyItemFragment
import pi.restaurant.management.utils.StringFormatUtils
import pi.restaurant.management.utils.Utils
import java.util.*

abstract class AbstractModifyDiscountFragment : AbstractModifyItemFragment() {

    private var _binding: FragmentModifyDiscountBinding? = null
    val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val saveButton get() = binding.buttonSave
    override val removeButton get() = binding.buttonRemove
    override var itemId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyDiscountBinding.inflate(inflater, container, false)
        linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun initializeUI() {
        finishLoading()
        initializeSpinner()
        setSaveButtonListener()
    }

    private fun initializeSpinner() {
        val spinner: Spinner = binding.spinnerType
        spinner.adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_view, R.id.itemTextView, DiscountType.getArrayOfStrings(requireContext()))
    }

    override fun getDataObject(): SplitDataObject {
        itemId = itemId.ifEmpty { StringFormatUtils.formatId() }
        val availableNumber = binding.editTextAvailable.text.toString().toInt()
        val assignedNumber = binding.editTextAssigned.text.toString().toInt()
        val usedNumber = binding.editTextUsed.text.toString().toInt()
        val code = binding.editTextCode.text.toString()

        val basic = DiscountBasic(
            id = code,
            availableDiscounts = Utils.createDiscounts(code, availableNumber, 0),
            assignedDiscounts = Utils.createDiscounts(code, assignedNumber, availableNumber),
            usedDiscounts = Utils.createDiscounts(code, usedNumber, availableNumber + assignedNumber),
            type = binding.spinnerType.selectedItemId.toInt(),
            amount = binding.editTextAmount.text.toString().toDouble(),
            expirationDate = Date() //TODO Zrobić
        )

        return SplitDataObject(code, basic, DiscountDetails())
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
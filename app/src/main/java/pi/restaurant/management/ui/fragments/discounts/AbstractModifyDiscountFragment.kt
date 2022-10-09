package pi.restaurant.management.ui.fragments.discounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import pi.restaurant.management.R
import pi.restaurant.management.objects.data.*
import pi.restaurant.management.objects.data.discount.DiscountBasic
import pi.restaurant.management.objects.data.discount.DiscountDetails
import pi.restaurant.management.databinding.FragmentModifyDiscountBinding
import pi.restaurant.management.logic.fragments.discounts.AbstractModifyDiscountViewModel
import pi.restaurant.management.objects.data.discount.Discount
import pi.restaurant.management.objects.enums.DiscountType
import pi.restaurant.management.ui.fragments.AbstractModifyItemFragment
import pi.restaurant.management.utils.StringFormatUtils
import java.util.*

abstract class AbstractModifyDiscountFragment : AbstractModifyItemFragment() {

    private var _binding: FragmentModifyDiscountBinding? = null
    val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val cardSetNavigation get() = binding.cardSetNavigation
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
        setNavigationCardsSave() // TODO Przy dodawaniu edycji uważać!
    }

    private fun initializeSpinner() {
        val spinner: Spinner = binding.spinnerType
        spinner.adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_view, R.id.itemTextView, DiscountType.getArrayOfStrings(requireContext()))
    }

    override fun getDataObject(): SplitDataObject {
        val discount = (viewModel as AbstractModifyDiscountViewModel).discount ?: Discount()
        itemId = itemId.ifEmpty { StringFormatUtils.formatId() }
        val availableNumber = binding.editTextAvailable.text.toString().toInt()
        val assignedNumber = binding.editTextAssigned.text.toString().toInt()
        val usedNumber = binding.editTextUsed.text.toString().toInt()
        val code = binding.editTextCode.text.toString()
        val discountsViewModel = viewModel as AbstractModifyDiscountViewModel

        val basic = DiscountBasic(
            id = code,
            availableDiscounts = discountsViewModel.createDiscounts(code, availableNumber, 0),
            assignedDiscounts = discountsViewModel.createDiscounts(code, assignedNumber, availableNumber),
            usedDiscounts = discountsViewModel.createDiscounts(code, usedNumber, availableNumber + assignedNumber),
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
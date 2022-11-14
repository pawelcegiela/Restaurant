package pi.restaurantapp.ui.fragments.management.discounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentModifyDiscountBinding
import pi.restaurantapp.model.activities.management.DiscountsViewModel
import pi.restaurantapp.model.fragments.management.discounts.AbstractModifyDiscountViewModel
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.data.discount.DiscountDetails
import pi.restaurantapp.objects.enums.DiscountReceiverType
import pi.restaurantapp.objects.enums.DiscountUsageType
import pi.restaurantapp.objects.enums.DiscountValueType
import pi.restaurantapp.objects.enums.Precondition
import pi.restaurantapp.ui.fragments.AbstractModifyItemFragment
import pi.restaurantapp.ui.pickers.DatePickerFragment
import pi.restaurantapp.ui.adapters.SpinnerAdapter
import pi.restaurantapp.utils.ComputingUtils
import pi.restaurantapp.utils.StringFormatUtils

abstract class AbstractModifyDiscountFragment : AbstractModifyItemFragment() {

    private var _binding: FragmentModifyDiscountBinding? = null
    val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation get() = binding.toolbarNavigation
    override var itemId = ""

    private val activityViewModel: DiscountsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyDiscountBinding.inflate(inflater, container, false)
        linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    override fun initializeUI() {
        initializeExpirationDate()
        finishLoading()
        initializeSpinner()
        setNavigationCardsSave()
    }

    fun initializeExpirationDate() {
        binding.textViewExpirationDate.text = ComputingUtils.getInitialExpirationDateString()
        binding.textViewExpirationDate.setOnClickListener {
            val date = ComputingUtils.getDateTimeFromString(binding.textViewExpirationDate.text.toString())
            DatePickerFragment(date) { newDate ->
                binding.textViewExpirationDate.text = StringFormatUtils.format(newDate, "00:00")
            }.show(requireActivity().supportFragmentManager, "datePicker")
        }
    }

    fun initializeSpinner() {
        binding.spinnerValueType.adapter = SpinnerAdapter(requireContext(), DiscountValueType.getArrayOfStrings(requireContext()))
        binding.spinnerReceiverType.adapter = SpinnerAdapter(requireContext(), DiscountReceiverType.getArrayOfStrings(requireContext()))
        binding.spinnerUsageType.adapter = SpinnerAdapter(requireContext(), DiscountUsageType.getArrayOfStrings(requireContext()))
    }

    override fun getDataObject(): SplitDataObject {
        val discount = (viewModel as AbstractModifyDiscountViewModel).getPreviousItem()
        itemId = itemId.ifEmpty { StringFormatUtils.formatId() }
        val availableNumber = binding.editTextTotalNumber.text.toString().toInt()
        val code = binding.editTextCode.text.toString()

        val basic = DiscountBasic(
            id = code,
            amount = binding.editTextAmount.text.toString(),
            valueType = binding.spinnerValueType.selectedItemId.toInt(),
            hasThreshold = binding.checkBoxThreshold.isChecked,
            thresholdValue = binding.editTextThreshold.text.toString().ifEmpty { "0.0" },
            creationDate = discount.basic.creationDate,
            expirationDate = ComputingUtils.getDateTimeFromString(binding.textViewExpirationDate.text.toString()),
            numberOfDiscounts = availableNumber,
            assignedDiscounts = discount.basic.assignedDiscounts,
            redeemedDiscounts = discount.basic.redeemedDiscounts,
            receiverType = binding.spinnerReceiverType.selectedItemId.toInt(),
            usageType = binding.spinnerUsageType.selectedItemId.toInt()
        )

        return SplitDataObject(code, basic, DiscountDetails())
    }

    override fun checkSavePreconditions(data: SplitDataObject): Precondition {
        if (super.checkSavePreconditions(data) != Precondition.OK) {
            return super.checkSavePreconditions(data)
        }
        val discount = data.basic as DiscountBasic
        if (activityViewModel.list.value?.any { it.id == discount.id && it.creationDate != discount.creationDate } == true) {
            return Precondition.DISCOUNT_CODE_EXISTS
        }
        return Precondition.OK
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextTotalNumber] = R.string.number_of_discounts
        map[binding.editTextCode] = R.string.code
        map[binding.editTextAmount] = R.string.amount
        return map
    }
}
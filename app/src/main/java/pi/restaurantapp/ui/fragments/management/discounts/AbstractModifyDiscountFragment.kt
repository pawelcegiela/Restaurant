package pi.restaurantapp.ui.fragments.management.discounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentModifyDiscountBinding
import pi.restaurantapp.logic.utils.ComputingUtils
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.ui.fragments.AbstractModifyItemFragment
import pi.restaurantapp.ui.pickers.DatePickerFragment
import pi.restaurantapp.viewmodels.activities.management.DiscountsViewModel
import pi.restaurantapp.viewmodels.fragments.management.discounts.AbstractModifyDiscountViewModel

abstract class AbstractModifyDiscountFragment : AbstractModifyItemFragment() {

    private var _binding: FragmentModifyDiscountBinding? = null
    val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation get() = binding.toolbarNavigation
    override var itemId = ""
    private val _viewModel get() = viewModel as AbstractModifyDiscountViewModel

    private val activityViewModel: DiscountsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyDiscountBinding.inflate(inflater, container, false)
        binding.vm = _viewModel
        binding.fragment = this
        binding.lifecycleOwner = this
        linearLayout.visibility = View.INVISIBLE
        _viewModel.setList(activityViewModel.list.value ?: ArrayList())
        return binding.root
    }

    override fun initializeUI() {
        finishLoading()
    }

    fun onClickExpirationDate() {
        val date = ComputingUtils.getDateTimeFromString(binding.textViewExpirationDate.text.toString())
        DatePickerFragment(date) { newDate ->
            binding.textViewExpirationDate.text = StringFormatUtils.format(newDate, "00:00")
        }.show(requireActivity().supportFragmentManager, "datePicker")
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextTotalNumber] = R.string.number_of_discounts
        map[binding.editTextCode] = R.string.code
        map[binding.editTextAmount] = R.string.amount
        return map
    }
}
package pi.restaurantapp.ui.fragments.management.workers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import pi.restaurantapp.R
import pi.restaurantapp.databinding.FragmentModifyWorkerBinding
import pi.restaurantapp.model.fragments.management.workers.AbstractModifyWorkerViewModel
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.data.user.UserDetails
import pi.restaurantapp.objects.enums.Precondition
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.ui.fragments.AbstractModifyItemFragment
import pi.restaurantapp.ui.adapters.SpinnerAdapter
import pi.restaurantapp.utils.PreconditionUtils
import pi.restaurantapp.utils.StringFormatUtils

abstract class AbstractModifyWorkerFragment : AbstractModifyItemFragment() {
    private var _binding: FragmentModifyWorkerBinding? = null
    val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val toolbarNavigation get() = binding.toolbarNavigation
    override var itemId = ""

    var disabled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyWorkerBinding.inflate(inflater, container, false)
        linearLayout.visibility = View.INVISIBLE
        return binding.root
    }

    fun initializeSpinner() {
        binding.spinnerRole.adapter = SpinnerAdapter(requireContext(), Role.getArrayOfStrings(requireContext()))
    }

    override fun getDataObject(): SplitDataObject {
        val user = (viewModel as AbstractModifyWorkerViewModel).getPreviousItem()
        itemId = itemId.ifEmpty { StringFormatUtils.formatId() }

        val basic = UserBasic(
            id = itemId,
            firstName = binding.editTextFirstName.text.toString(),
            lastName = binding.editTextLastName.text.toString(),
            role = binding.spinnerRole.selectedItemId.toInt(),
            disabled = user.basic.disabled,
            delivery = binding.checkBoxDelivery.isChecked
        )
        val details = UserDetails(
            id = itemId,
            email = binding.editTextEmail.text.toString(),
            creationDate = user.details.creationDate,
            ordersToDeliver = user.details.ordersToDeliver
        )

        return SplitDataObject(itemId, basic, details)
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextFirstName] = R.string.first_name
        map[binding.editTextLastName] = R.string.last_name
        map[binding.editTextEmail] = R.string.e_mail
        return map
    }

    override fun checkSavePreconditions(data: SplitDataObject): Precondition {
        if (super.checkSavePreconditions(data) != Precondition.OK) {
            return super.checkSavePreconditions(data)
        }
        return PreconditionUtils.compareRoles(viewModel.userRole.value!!, (data.basic as UserBasic).role)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package pi.restaurant.management.fragments.workers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import pi.restaurant.management.R
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.User
import pi.restaurant.management.databinding.FragmentModifyWorkerBinding
import pi.restaurant.management.enums.DiscountType
import pi.restaurant.management.enums.Precondition
import pi.restaurant.management.enums.Role
import pi.restaurant.management.fragments.AbstractModifyItemFragment
import pi.restaurant.management.utils.PreconditionUtils

abstract class AbstractModifyWorkerFragment : AbstractModifyItemFragment() {
    private var _binding: FragmentModifyWorkerBinding? = null
    val binding get() = _binding!!

    override val linearLayout get() = binding.linearLayout
    override val progressBar get() = binding.progress.progressBar
    override val saveButton get() = binding.buttonSave
    override val removeButton get() = binding.buttonRemove
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
        binding.spinnerRole.adapter =
            ArrayAdapter(requireContext(), R.layout.spinner_item_view, R.id.itemTextView, Role.getArrayOfStrings(requireContext()))
    }

    override fun getDataObject(): AbstractDataObject {
        val firstName = binding.editTextFirstName.text.toString()
        val lastName = binding.editTextLastName.text.toString()
        val email = binding.editTextEmail.text.toString()
        val role = binding.spinnerRole.selectedItemId.toInt()

        return User(itemId, firstName, lastName, email, role, disabled)
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextFirstName] = R.string.first_name
        map[binding.editTextLastName] = R.string.last_name
        map[binding.editTextEmail] = R.string.e_mail
        return map
    }

    override fun checkSavePreconditions(data: AbstractDataObject): Precondition {
        if (super.checkSavePreconditions(data) != Precondition.OK) {
            return super.checkSavePreconditions(data)
        }
        return PreconditionUtils.compareRoles(myRole, (data as User).role)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
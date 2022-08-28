package pi.restaurant.management.fragments.workers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import pi.restaurant.management.R
import pi.restaurant.management.data.UserData
import pi.restaurant.management.databinding.FragmentUserDataBinding
import pi.restaurant.management.fragments.ModifyItemFragment
import pi.restaurant.management.utils.Utils

abstract class ModifyWorkerFragment : ModifyItemFragment() {
    private var _binding: FragmentUserDataBinding? = null
    val binding get() = _binding!!

    override val databasePath = "discounts"
    override val linearLayout get() = binding.linearLayout
    override val saveButton get() = binding.buttonSaveData
    override var itemId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    open fun initializeSpinner() {
        val spinner: Spinner = binding.spinnerRole
        ArrayAdapter.createFromResource(
            context!!,
            R.array.roles,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    fun setSaveButtonListener() {
        binding.buttonSaveData.setOnClickListener {
            if (!Utils.checkRequiredFields(getEditTextMap(), this)) {
                return@setOnClickListener
            }

            val firstName = binding.editTextFirstName.text.toString()
            val lastName = binding.editTextLastName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val role = binding.spinnerRole.selectedItemId.toInt()

            setValue(UserData(itemId, firstName, lastName, email, role))
        }
    }

    open fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextFirstName] = R.string.first_name
        map[binding.editTextLastName] = R.string.last_name
        map[binding.editTextEmail] = R.string.e_mail
        return map
    }

    abstract fun setValue(data: UserData)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
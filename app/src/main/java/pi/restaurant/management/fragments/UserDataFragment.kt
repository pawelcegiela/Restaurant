package pi.restaurant.management.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.data.UserData
import pi.restaurant.management.databinding.FragmentUserDataBinding

abstract class UserDataFragment : Fragment() {
    private var _binding: FragmentUserDataBinding? = null
    val binding get() = _binding!!
    var id: String = ""

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

    fun loadData() {
        val databaseRef = Firebase.database.getReference("users").child(id)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue<UserData>() ?: return
                binding.editTextFirstName.setText(data.firstName)
                binding.editTextLastName.setText(data.lastName)
                binding.editTextEmail.setText(data.email)
                binding.spinnerRole.setSelection(data.role)
                if (data.disabled) {
                    binding.buttonDisableUser.text = getString(R.string.enable_user)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun setSaveButtonListener() {
        binding.buttonSaveData.setOnClickListener {
            val firstName = binding.editTextFirstName.text.toString()
            val lastName = binding.editTextLastName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val role = binding.spinnerRole.selectedItemId.toInt()

            if (checkRequiredFields(
                    firstName.trim().isEmpty(),
                    lastName.trim().isEmpty(),
                    email.trim().isEmpty()
                )
            ) {
                return@setOnClickListener
            }

            setValue(UserData(id, firstName, lastName, email, role))
        }
    }

    private fun checkRequiredFields(
        firstNameEmpty: Boolean,
        lastNameEmpty: Boolean,
        emailEmpty: Boolean
    ): Boolean {
        if (firstNameEmpty) {
            binding.editTextFirstName.error =
                getString(R.string.is_required, getString(R.string.first_name))
        }
        if (lastNameEmpty) {
            binding.editTextLastName.error =
                getString(R.string.is_required, getString(R.string.last_name))
        }
        if (emailEmpty) {
            binding.editTextEmail.error =
                getString(R.string.is_required, getString(R.string.e_mail))
        }
        return (firstNameEmpty || lastNameEmpty || emailEmpty)
    }

    abstract fun setValue(data: UserData)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
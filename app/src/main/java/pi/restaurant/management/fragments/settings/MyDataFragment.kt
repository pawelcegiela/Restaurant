package pi.restaurant.management.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.data.UserData
import pi.restaurant.management.databinding.FragmentUserDataBinding
import pi.restaurant.management.fragments.AbstractSplashScreenFragment
import pi.restaurant.management.utils.Utils


//TODO: Ten fragment powinien dziedziczyć po UserDataFragment (ModifyWorkerFragment). Należy przemyśleć jak to rozpracować

class MyDataFragment : AbstractSplashScreenFragment() {

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

    private fun initializeSpinner() {
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

    private fun loadData() {
        val databaseRef = Firebase.database.getReference("users").child(id)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue<UserData>() ?: return
                binding.editTextFirstName.setText(data.firstName)
                binding.editTextLastName.setText(data.lastName)
                binding.editTextEmail.setText(data.email)
                binding.spinnerRole.setSelection(data.role)
                if (data.disabled) {
                    binding.buttonRemove.text = getString(R.string.enable_user)
                }
                keepSplashScreen = false
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun setSaveButtonListener() {
        binding.buttonSave.setOnClickListener {
            if (!Utils.checkRequiredFields(getEditTextMap(), this)) {
                return@setOnClickListener
            }

            val firstName = binding.editTextFirstName.text.toString()
            val lastName = binding.editTextLastName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val role = binding.spinnerRole.selectedItemId.toInt()

            setValue(UserData(id, firstName, lastName, email, role))
        }
    }

    private fun getEditTextMap(): Map<EditText, Int> {
        val map = HashMap<EditText, Int>()
        map[binding.editTextFirstName] = R.string.first_name
        map[binding.editTextLastName] = R.string.last_name
        map[binding.editTextEmail] = R.string.e_mail
        return map
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id = Firebase.auth.currentUser?.uid ?: return

        binding.spinnerRole.isEnabled = false
        binding.editTextUserPassword.visibility = View.GONE
        binding.editTextRepeatUserPassword.visibility = View.GONE
        binding.buttonRemove.visibility = View.GONE

        initializeSpinner()
        loadData()
        setSaveButtonListener()
    }

    fun setValue(data: UserData) {
        val user = Firebase.auth.currentUser ?: return
        val databaseRef = Firebase.database.getReference("users").child(id)
        val password = binding.editTextPassword.text.toString()
        if (user.email == null || password.isEmpty()) {
            binding.editTextPassword.error = getString(R.string.enter_password)
            return
        }

        val credential = EmailAuthProvider.getCredential(user.email!!, password)

        user.reauthenticate(credential)
            .addOnCompleteListener { task ->
                binding.editTextPassword.setText("")
                if (task.isSuccessful) {
                    user.updateEmail(data.email).addOnCompleteListener {
                        databaseRef.setValue(data)
                        Toast.makeText(
                            activity,
                            getString(R.string.your_data_has_been_changed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        activity,
                        getString(R.string.auth_failed_try_again),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
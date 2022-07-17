package pi.restaurant.management.fragments.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.data.UserData
import pi.restaurant.management.fragments.UserDataFragment


class MyDataFragment : UserDataFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id = Firebase.auth.currentUser?.uid ?: return

        binding.spinnerRole.isEnabled = false
        binding.editTextUserPassword.visibility = View.GONE
        binding.editTextRepeatUserPassword.visibility = View.GONE
        binding.buttonDisableUser.visibility = View.GONE

        initializeSpinner()
        loadData()
        setSaveButtonListener()
    }

    override fun setValue(data: UserData) {
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
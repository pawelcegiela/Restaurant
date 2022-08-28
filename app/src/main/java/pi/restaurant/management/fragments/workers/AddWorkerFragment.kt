package pi.restaurant.management.fragments.workers

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.data.UserData

class AddWorkerFragment : AbstractModifyWorkerFragment() {

    override val nextActionId = R.id.actionAddWorkerToWorkers
    override val saveMessageId = 0 // Warning: unused
    override val removeMessageId = 0 // Warning: unused

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editTextPassword.visibility = View.GONE //TODO: Temp
        removeButton.visibility = View.GONE
    }

    override fun initializeUI() {
        initializeSpinner()
        setSaveButtonListener()
    }

    override fun setValue(data: UserData) {
        //TODO: Żeby nie przelogowywało

        if (data.role <= myRole) {
            Toast.makeText(
                activity,
                getString(R.string.you_can_set_lower_roles),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val password = binding.editTextUserPassword.text.toString()
        val passwordRepeated = binding.editTextRepeatUserPassword.text.toString()

        if (data.email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                activity,
                getString(R.string.enter_email_password),
                Toast.LENGTH_SHORT
            ).show()
        } else if (password != passwordRepeated) {
            Toast.makeText(activity, getString(R.string.passwords_differ), Toast.LENGTH_SHORT)
                .show()
        } else {
            Firebase.auth.createUserWithEmailAndPassword(data.email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    data.id = Firebase.auth.currentUser!!.uid
                    val databaseRef = Firebase.database.getReference("users").child(data.id)
                    databaseRef.setValue(data)
                    Toast.makeText(context, getString(R.string.created_new_user, data.firstName + " " + data.lastName),
                        Toast.LENGTH_LONG).show()
                    findNavController().navigate(nextActionId)
                } else {
                    Toast.makeText(context, getString(R.string.authentication_failed),
                        Toast.LENGTH_SHORT).show()
                    binding.editTextUserPassword.setText("")
                    binding.editTextRepeatUserPassword.setText("")
                }
            }
        }
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = super.getEditTextMap().toMutableMap()
        map[binding.editTextUserPassword] = R.string.user_password
        map[binding.editTextRepeatUserPassword] = R.string.repeat_user_password
        return map
    }
}
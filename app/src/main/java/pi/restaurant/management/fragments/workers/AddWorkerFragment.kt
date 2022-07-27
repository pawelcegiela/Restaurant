package pi.restaurant.management.fragments.workers

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.data.UserData
import pi.restaurant.management.fragments.UserDataFragment

class AddWorkerFragment : UserDataFragment() {
    private var myRole: Int = 3

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO: Zablokowanie dla Workerów
        binding.editTextPassword.visibility = View.GONE //TODO: Temp
        binding.buttonDisableUser.visibility = View.GONE

        val databaseRef = Firebase.database.getReference("users").child(Firebase.auth.currentUser!!.uid).child("role")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                myRole = dataSnapshot.getValue<Int>() ?: return
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        initializeSpinner()
        setSaveButtonListener()
        keepSplashScreen = false
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
                    findNavController().navigate(R.id.actionAddWorkerToWorkers)
                } else {
                    Toast.makeText(context, getString(R.string.authentication_failed),
                        Toast.LENGTH_SHORT).show()
                    binding.editTextUserPassword.setText("")
                    binding.editTextRepeatUserPassword.setText("")
                }
            }
        }
    }
}
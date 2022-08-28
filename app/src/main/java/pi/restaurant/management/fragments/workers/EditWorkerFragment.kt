package pi.restaurant.management.fragments.workers

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.data.UserData

class EditWorkerFragment : AbstractModifyWorkerFragment() {

    override val saveActionId = R.id.actionEditWorkerToWorkers
    override val toastMessageId = 0 // Warning: unused

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemId = arguments?.getString("id").toString()
        binding.editTextEmail.isEnabled = false
        binding.editTextUserPassword.visibility = View.GONE
        binding.editTextRepeatUserPassword.visibility = View.GONE
        binding.editTextPassword.visibility = View.GONE
    }

    override fun initializeUI() {
        initializeSpinner()
        loadData()
        setSaveButtonListener()
        setDisableButtonListener()
    }

    override fun setData(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<UserData>() ?: return
        binding.editTextFirstName.setText(data.firstName)
        binding.editTextLastName.setText(data.lastName)
        binding.editTextEmail.setText(data.email)
        binding.spinnerRole.setSelection(data.role)
        if (data.disabled) {
            binding.buttonDisableUser.text = getString(R.string.enable_user)
        }
    }

    override fun setValue(data: UserData) {
        if (data.role <= myRole) {
            Toast.makeText(
                activity,
                getString(R.string.you_can_set_lower_roles),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val databaseRef = Firebase.database.getReference("users").child(itemId)
        databaseRef.setValue(data)

        Toast.makeText(activity, getString(R.string.user_data_has_been_changed), Toast.LENGTH_SHORT)
            .show()

        findNavController().navigate(saveActionId)
    }

    private fun setDisableButtonListener() {
        binding.buttonDisableUser.setOnClickListener {
            val databaseRef = Firebase.database.getReference("users").child(itemId)
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val data = dataSnapshot.getValue<UserData>() ?: return
                    data.disabled = !data.disabled
                    setValue(data)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }
}
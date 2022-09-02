package pi.restaurant.management.fragments.workers

import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.User

class EditWorkerFragment : AbstractModifyWorkerFragment() {

    override val nextActionId = R.id.actionEditWorkerToWorkers
    override val saveMessageId = R.string.worker_modified
    override val removeMessageId = 0 // Warning: unused

    override fun initializeUI() {
        itemId = arguments?.getString("id").toString()

        binding.editTextEmail.isEnabled = false
        binding.editTextUserPassword.visibility = View.GONE
        binding.editTextRepeatUserPassword.visibility = View.GONE
        binding.editTextPassword.visibility = View.GONE

        initializeSpinner()
        getDataFromDatabase()
        setSaveButtonListener()
        setRemoveButtonListener()
    }

    override fun fillInData(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<User>() ?: return
        binding.editTextFirstName.setText(data.firstName)
        binding.editTextLastName.setText(data.lastName)
        binding.editTextEmail.setText(data.email)
        binding.spinnerRole.setSelection(data.role)

        disabled = data.disabled
        if (data.disabled) {
            removeButton.text = getString(R.string.enable_user)
        } else {
            removeButton.text = getText(R.string.disable_user)
        }
        binding.progress.progressBar.visibility = View.GONE
    }

    override fun setRemoveButtonListener() {
        removeButton.setOnClickListener {
            disabled = !disabled
            saveToDatabase()
        }
    }
}
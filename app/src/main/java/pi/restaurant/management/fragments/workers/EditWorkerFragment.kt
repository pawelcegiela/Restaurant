package pi.restaurant.management.fragments.workers

import android.view.View
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.activities.SettingsActivity
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.User
import pi.restaurant.management.enums.Precondition

class EditWorkerFragment : AbstractModifyWorkerFragment() {

    override var nextActionId = R.id.actionEditWorkerToWorkers
    override val saveMessageId = R.string.worker_modified
    override val removeMessageId = 0 // Warning: unused
    private var isMyData = false

    override fun initializeUI() {
        itemId = arguments?.getString("id").toString()
        isMyData = arguments?.getBoolean("myData") != null && arguments?.getBoolean("myData")!!
        if (isMyData) {
            setMyDataSettings()
        }

        binding.editTextEmail.isEnabled = false
        binding.editTextUserPassword.visibility = View.GONE
        binding.editTextRepeatUserPassword.visibility = View.GONE
        binding.editTextPassword.visibility = View.GONE

        initializeSpinner()
        getDataFromDatabase()
        setSaveButtonListener()
        setRemoveButtonListener()
    }

    private fun setMyDataSettings() {
        itemId = Firebase.auth.uid ?: return
        binding.buttonRemove.visibility = View.GONE
        binding.spinnerRole.isEnabled = false
        nextActionId = if (activity is SettingsActivity) {
            R.id.actionMyDataToSettings
        } else {
            R.id.actionEditMyDataToWorkers
        }
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

    override fun checkSavePreconditions(data: AbstractDataObject): Precondition {
        return if (isMyData) {
            Precondition.OK
        } else {
            super.checkSavePreconditions(data)
        }
    }

    override fun setRemoveButtonListener() {
        removeButton.setOnClickListener {
            disabled = !disabled
            saveToDatabase()
        }
    }
}
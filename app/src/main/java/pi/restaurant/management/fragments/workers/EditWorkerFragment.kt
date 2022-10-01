package pi.restaurant.management.fragments.workers

import android.view.View
import androidx.fragment.app.viewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.activities.SettingsActivity
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.User
import pi.restaurant.management.data.UserBasic
import pi.restaurant.management.data.UserDetails
import pi.restaurant.management.enums.Precondition
import pi.restaurant.management.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.utils.SnapshotsPair

class EditWorkerFragment : AbstractModifyWorkerFragment() {

    override var nextActionId = R.id.actionEditWorkerToWorkers
    override val saveMessageId = R.string.worker_modified
    override val removeMessageId = 0 // Warning: unused
    private var isMyData = false
    override val viewModel : AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel : EditWorkerViewModel by viewModels()

    override fun initializeUI() {
        itemId = arguments?.getString("id").toString()
        isMyData = arguments?.getBoolean("myData") != null && arguments?.getBoolean("myData")!!
        if (isMyData) {
            setNavigationCardsSave()
            setMyDataSettings()
        } else {
            setNavigationCardsSaveRemove()
        }

        binding.editTextEmail.isEnabled = false
        binding.editTextUserPassword.visibility = View.GONE
        binding.editTextRepeatUserPassword.visibility = View.GONE
        binding.editTextPassword.visibility = View.GONE

        initializeSpinner()
    }

    private fun setMyDataSettings() {
        itemId = Firebase.auth.uid ?: return
        binding.spinnerRole.isEnabled = false
        nextActionId = if (activity is SettingsActivity) {
            R.id.actionMyDataToSettings
        } else {
            R.id.actionEditMyDataToWorkers
        }
    }

    private fun getItem(snapshotsPair: SnapshotsPair) : User {
        val basic = snapshotsPair.basic?.getValue<UserBasic>() ?: UserBasic()
        val details = snapshotsPair.details?.getValue<UserDetails>() ?: UserDetails()
        return User(itemId, basic, details)
    }

    override fun fillInData(snapshotsPair: SnapshotsPair) {
        val data = getItem(snapshotsPair)
        binding.editTextFirstName.setText(data.basic.firstName)
        binding.editTextLastName.setText(data.basic.lastName)
        binding.editTextEmail.setText(data.details.email)
        binding.spinnerRole.setSelection(data.basic.role)

        creationDate = data.details.creationDate
        disabled = data.basic.disabled
//        if (disabled) {
//            cardSetNavigation.cardSaveRemoveBack.cardRemove.textViewRemove.text = getString(R.string.enable_user)
//        } else {
//            cardSetNavigation.cardSaveRemoveBack.cardRemove.textViewRemove.text = getText(R.string.disable_user)
//        } TODO Przywrócić
    }

    override fun checkSavePreconditions(data: AbstractDataObject): Precondition {
        return if (isMyData) {
            Precondition.OK
        } else {
            super.checkSavePreconditions(data)
        }
    }

    override fun setNavigationCardsSaveRemove() {
        super.setNavigationCardsSaveRemove()
        cardSetNavigation.cardSaveRemoveBack.cardRemove.setOnClickListener {
            disabled = !disabled
            saveToDatabase()
        }
    }
}
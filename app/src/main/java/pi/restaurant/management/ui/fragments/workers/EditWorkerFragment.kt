package pi.restaurant.management.ui.fragments.workers

import android.view.View
import androidx.fragment.app.viewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.model.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.model.fragments.workers.EditWorkerViewModel
import pi.restaurant.management.objects.data.SplitDataObject
import pi.restaurant.management.objects.data.user.User
import pi.restaurant.management.objects.enums.Precondition
import pi.restaurant.management.ui.activities.SettingsActivity

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
            setMyDataSettings()
        }

        binding.editTextEmail.isEnabled = false
        binding.linearLayoutPasswords.visibility = View.GONE

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

    override fun fillInData() {
        val data = _viewModel.item.value ?: User()
        binding.editTextFirstName.setText(data.basic.firstName)
        binding.editTextLastName.setText(data.basic.lastName)
        binding.editTextEmail.setText(data.details.email)
        binding.spinnerRole.setSelection(data.basic.role)
        binding.checkBoxDelivery.isChecked = data.basic.delivery

        disabled = data.basic.disabled
//        if (disabled) {
//            toolbarNavigation.cardSaveRemoveBack.cardRemove.textViewRemove.text = getString(R.string.enable_user)
//        } else {
//            toolbarNavigation.cardSaveRemoveBack.cardRemove.textViewRemove.text = getText(R.string.disable_user)
//        } TODO Przywrócić
        if (isMyData) {
            setNavigationCardsSave()
        } else {
            setNavigationCardsSaveRemove()
        }
    }

    override fun checkSavePreconditions(data: SplitDataObject): Precondition {
        return if (isMyData) {
            Precondition.OK
        } else {
            super.checkSavePreconditions(data)
        }
    }

    override fun setNavigationCardsSaveRemove() {
        super.setNavigationCardsSaveRemove()
        toolbarNavigation.cardSaveRemove.cardRemove.setOnClickListener {
            disabled = !disabled
            saveToDatabase()
        }
    }
}
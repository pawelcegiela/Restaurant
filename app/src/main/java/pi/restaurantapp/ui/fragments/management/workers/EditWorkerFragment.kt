package pi.restaurantapp.ui.fragments.management.workers

import androidx.fragment.app.viewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.R
import pi.restaurantapp.model.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.model.fragments.management.workers.EditWorkerViewModel
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.enums.Precondition
import pi.restaurantapp.ui.activities.management.SettingsActivity

class EditWorkerFragment : AbstractModifyWorkerFragment() {

    override var nextActionId = R.id.actionEditWorkerToWorkers
    override val saveMessageId = R.string.worker_modified
    override val removeMessageId = 0 // Warning: unused
    override val viewModel: AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel: EditWorkerViewModel by viewModels()

    override fun initializeUI() {
        _viewModel.isMyData = arguments?.getBoolean("myData") != null && arguments?.getBoolean("myData")!!
        itemId = if (_viewModel.isMyData) Firebase.auth.uid!! else arguments?.getString("id").toString()

        if (_viewModel.isMyData) {
            nextActionId = if (activity is SettingsActivity) {
                R.id.actionMyDataToSettings
            } else {
                R.id.actionEditMyDataToWorkers
            }
        }
    }

    override fun fillInData() {
        if (_viewModel.isMyData) {
            setNavigationCardsSave()
        } else {
            setNavigationCardsSaveRemove()
        }
    }

    override fun checkSavePreconditions(data: SplitDataObject): Precondition {
        return if (_viewModel.isMyData) {
            Precondition.OK
        } else {
            super.checkSavePreconditions(data)
        }
    }
}
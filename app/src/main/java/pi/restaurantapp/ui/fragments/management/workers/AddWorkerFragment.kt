package pi.restaurantapp.ui.fragments.management.workers

import android.widget.EditText
import androidx.fragment.app.viewModels
import pi.restaurantapp.R
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.workers.AddWorkerViewModel

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for AddWorkerFragment.
 * @see pi.restaurantapp.viewmodels.fragments.management.workers.AddWorkerViewModel ViewModel layer
 * @see pi.restaurantapp.logic.fragments.management.workers.AddWorkerLogic Model layer
 */
class AddWorkerFragment : AbstractModifyWorkerFragment() {

    override val nextActionId = R.id.actionAddWorkerToWorkers
    override val saveMessageId = R.string.created_new_user
    override val removeMessageId = 0 // Unused
    override val viewModel: AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel: AddWorkerViewModel by viewModels()

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = super.getEditTextMap().toMutableMap()
        map[binding.editTextUserPassword] = R.string.user_password
        map[binding.editTextRepeatUserPassword] = R.string.repeat_user_password
        return map
    }
}
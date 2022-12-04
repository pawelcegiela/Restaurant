package pi.restaurantapp.ui.fragments.management.workers

import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.R
import pi.restaurantapp.logic.utils.PreconditionUtils
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.user.UserDetails
import pi.restaurantapp.objects.enums.Precondition
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.viewmodels.fragments.management.workers.AddWorkerViewModel

class AddWorkerFragment : AbstractModifyWorkerFragment() {

    override val nextActionId = R.id.actionAddWorkerToWorkers
    override val saveMessageId = R.string.created_new_user
    override val removeMessageId = 0 // Unused
    override val viewModel: AbstractModifyItemViewModel get() = _viewModel
    private val _viewModel: AddWorkerViewModel by viewModels()

    override fun initializeUI() {
        finishLoading()
        setNavigationCardsSave()
    }

    override fun saveToDatabase() {
        val data = getDataObject()

        val precondition = checkSavePreconditions(data)
        if (precondition != Precondition.OK) {
            Toast.makeText(activity, getString(precondition.nameRes), Toast.LENGTH_SHORT).show()
            return
        }

        val password = binding.editTextUserPassword.text.toString()
        Firebase.auth.createUserWithEmailAndPassword((data.details as UserDetails).email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //TODO: No logging out
                    _viewModel.setNewUserId()
                    super.saveToDatabase()
                } else {
                    Toast.makeText(
                        context, getString(R.string.authentication_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.editTextUserPassword.setText("")
                    binding.editTextRepeatUserPassword.setText("")
                }
            }
    }

    override fun checkSavePreconditions(data: SplitDataObject): Precondition {
        if (super.checkSavePreconditions(data) != Precondition.OK) {
            return super.checkSavePreconditions(data)
        }
        val password = binding.editTextUserPassword.text.toString()
        val repeatedPassword = binding.editTextRepeatUserPassword.text.toString()
        return PreconditionUtils.checkUserPasswords(password, repeatedPassword)
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = super.getEditTextMap().toMutableMap()
        map[binding.editTextUserPassword] = R.string.user_password
        map[binding.editTextRepeatUserPassword] = R.string.repeat_user_password
        return map
    }
}
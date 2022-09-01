package pi.restaurant.management.fragments.workers

import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.User
import pi.restaurant.management.enums.Precondition
import pi.restaurant.management.utils.Utils

class AddWorkerFragment : AbstractModifyWorkerFragment() {

    override val nextActionId = R.id.actionAddWorkerToWorkers
    override val saveMessageId = R.string.created_new_user
    override val removeMessageId = 0 // Warning: unused

    override fun initializeUI() {
        removeButton.visibility = View.GONE

        initializeSpinner()
        setSaveButtonListener()
    }

    //TODO Pomysł na większe połączenie kodu z superklasą
    override fun saveToDatabase() {
        val data = getDataObject() as User

        val precondition = checkSavePreconditions(data)
        if (precondition != Precondition.OK) {
            Toast.makeText(activity, getString(precondition.nameRes), Toast.LENGTH_SHORT).show()
            return
        }

        val password = binding.editTextUserPassword.text.toString()
        Firebase.auth.createUserWithEmailAndPassword(data.email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //TODO: Żeby nie przelogowywało
                    itemId = Firebase.auth.currentUser!!.uid
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

    override fun checkSavePreconditions(data: AbstractDataObject): Precondition {
        if (super.checkSavePreconditions(data) != Precondition.OK) {
            return super.checkSavePreconditions(data)
        }
        val password = binding.editTextUserPassword.text.toString()
        val repeatedPassword = binding.editTextRepeatUserPassword.text.toString()
        return Utils.checkUserPasswords(password, repeatedPassword)
    }

    override fun getEditTextMap(): Map<EditText, Int> {
        val map = super.getEditTextMap().toMutableMap()
        map[binding.editTextUserPassword] = R.string.user_password
        map[binding.editTextRepeatUserPassword] = R.string.repeat_user_password
        return map
    }
}
package pi.restaurantapp.viewmodels.fragments.management.workers

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.R
import pi.restaurantapp.logic.fragments.management.workers.AddWorkerLogic
import pi.restaurantapp.logic.utils.PreconditionUtils
import pi.restaurantapp.objects.data.user.User
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.data.user.UserDetails
import pi.restaurantapp.objects.enums.Precondition
import pi.restaurantapp.objects.enums.ToolbarType

class AddWorkerViewModel : AbstractModifyWorkerViewModel() {
    override val logic = AddWorkerLogic()

    override fun createItem() {
        setItem(User("", UserBasic(), UserDetails()))
        setReadyToUnlock()

        toolbarType.value = ToolbarType.SAVE
    }

    override fun saveToDatabase() {
        val precondition = checkSavePreconditions()
        if (precondition != Precondition.OK) {
            setToastMessage(precondition.nameRes)
            return
        }

        val password = userPassword.value!!
        Firebase.auth.createUserWithEmailAndPassword(item.value?.details?.email ?: return, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    setNewUserId()
                    super.saveToDatabase()
                } else {
                    setToastMessage(R.string.authentication_failed)
                    userPassword.value = ""
                    repeatUserPassword.value = ""
                }
            }
    }

    private fun setNewUserId() {
        itemId = Firebase.auth.uid!!
        item.value!!.id = itemId
        item.value!!.basic.id = itemId
        item.value!!.details.id = itemId
    }

    override fun checkSavePreconditions(): Precondition {
        if (super.checkSavePreconditions() != Precondition.OK) {
            return super.checkSavePreconditions()
        }
        return PreconditionUtils.checkUserPasswords(userPassword.value ?: "", repeatUserPassword.value ?: "")
    }
}
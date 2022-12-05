package pi.restaurantapp.viewmodels.fragments.management.workers

import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.management.workers.EditWorkerLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.user.User
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.data.user.UserDetails
import pi.restaurantapp.objects.enums.Precondition
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.objects.enums.ToolbarType

class EditWorkerViewModel : AbstractModifyWorkerViewModel() {
    override val logic = EditWorkerLogic()

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<UserBasic>() ?: UserBasic()
        val details = snapshotsPair.details?.toObject<UserDetails>() ?: UserDetails()
        setItem(User(itemId, basic, details))

        toolbarType.value = if (Role.isAtLeastExecutive(userRole.value) && !isMyData) ToolbarType.SAVE_REMOVE else ToolbarType.SAVE
    }

    override fun checkSavePreconditions(): Precondition {
        return if (isMyData) {
            Precondition.OK
        } else {
            super.checkSavePreconditions()
        }
    }
}
package pi.restaurant.management.ui.fragments.workers

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.objects.data.AbstractDataObject
import pi.restaurant.management.objects.data.user.UserBasic
import pi.restaurant.management.objects.enums.Precondition
import pi.restaurant.management.objects.enums.Role
import pi.restaurant.management.model.fragments.AbstractItemListViewModel

class WorkersMainViewModel : AbstractItemListViewModel() {
    override val databasePath = "users"

    override fun retrieveDataList(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<HashMap<String, UserBasic>>() ?: HashMap()
        setDataList(data.toList().map { it.second }.toMutableList())
    }

    override fun checkPreconditions(item: AbstractDataObject) {
        val user = item as UserBasic
        if (user.role > (userRole.value ?: Role.WORKER.ordinal)) {
            setEditPrecondition(Precondition.OK)
        } else if (user.id == Firebase.auth.uid) {
            setEditPrecondition(Precondition.SAME_USER)
        } else {
            setEditPrecondition(Precondition.TOO_LOW_ROLE)
        }
    }
}
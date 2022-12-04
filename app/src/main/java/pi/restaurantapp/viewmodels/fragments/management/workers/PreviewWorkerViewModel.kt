package pi.restaurantapp.viewmodels.fragments.management.workers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.logic.fragments.management.workers.PreviewWorkerLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.user.User
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.data.user.UserDetails
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel

class PreviewWorkerViewModel : AbstractPreviewItemViewModel() {
    override val logic = PreviewWorkerLogic()

    private val _item: MutableLiveData<User> = MutableLiveData()
    val item: LiveData<User> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<UserBasic>() ?: UserBasic()
        val details = snapshotsPair.details?.toObject<UserDetails>() ?: UserDetails()
        _item.value = User(itemId, basic, details)

        val myRole = userRole.value ?: Role.getPlaceholder()
        editable = myRole < basic.role || Firebase.auth.uid == itemId

        setReadyToUnlock()
    }

    override fun isDisabled(): Boolean {
        return item.value?.basic?.disabled == true
    }
}
package pi.restaurantapp.model.fragments.management.workers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.model.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.user.User
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.data.user.UserDetails

class PreviewWorkerViewModel : AbstractPreviewItemViewModel() {
    override val databasePath = "users"

    private val _item: MutableLiveData<User> = MutableLiveData()
    val item: LiveData<User> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<UserBasic>() ?: UserBasic()
        val details = snapshotsPair.details?.toObject<UserDetails>() ?: UserDetails()
        _item.value = User(itemId, basic, details)
    }

    override fun isDisabled(): Boolean {
        return item.value?.basic?.disabled == true
    }
}
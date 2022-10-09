package pi.restaurant.management.model.fragments.workers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.model.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.objects.SnapshotsPair
import pi.restaurant.management.objects.data.user.User
import pi.restaurant.management.objects.data.user.UserBasic
import pi.restaurant.management.objects.data.user.UserDetails

class PreviewWorkerViewModel : AbstractPreviewItemViewModel() {
    override val databasePath = "users"

    private val _item: MutableLiveData<User> = MutableLiveData()
    val item: LiveData<User> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.getValue<UserBasic>() ?: UserBasic()
        val details = snapshotsPair.details?.getValue<UserDetails>() ?: UserDetails()
        _item.value = User(itemId, basic, details)
    }

}
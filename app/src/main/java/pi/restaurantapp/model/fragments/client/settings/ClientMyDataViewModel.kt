package pi.restaurantapp.model.fragments.client.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.model.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.user.User
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.data.user.UserDetails

class ClientMyDataViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "users"

    private val _item: MutableLiveData<User> = MutableLiveData()
    val item: LiveData<User> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<UserBasic>() ?: UserBasic()
        val details = snapshotsPair.details?.toObject<UserDetails>() ?: UserDetails()
        _item.value = User(itemId, basic, details)
    }

    fun getPreviousItem(): User {
        return item.value ?: User(itemId, UserBasic(), UserDetails())
    }
}
package pi.restaurantapp.model.fragments.client.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ktx.getValue
import pi.restaurantapp.model.fragments.management.AbstractModifyItemViewModel
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.user.User
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.data.user.UserDetails

class ClientMyDataViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "users"

    private val _item: MutableLiveData<User> = MutableLiveData()
    val item: LiveData<User> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.getValue<UserBasic>() ?: UserBasic()
        val details = snapshotsPair.details?.getValue<UserDetails>() ?: UserDetails()
        _item.value = User(itemId, basic, details)
    }

    fun getPreviousItem(): User {
        return item.value ?: User(itemId, UserBasic(), UserDetails())
    }
}
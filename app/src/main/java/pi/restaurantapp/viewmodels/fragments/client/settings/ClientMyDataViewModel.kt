package pi.restaurantapp.viewmodels.fragments.client.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.client.settings.ClientMyDataLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.NullableSplitDataObject
import pi.restaurantapp.objects.data.user.User
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.data.user.UserDetails
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.objects.enums.ToolbarType
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for ClientMyDataFragment.
 * @see pi.restaurantapp.logic.fragments.client.settings.ClientMyDataLogic Model layer
 * @see pi.restaurantapp.ui.fragments.client.settings.ClientMyDataFragment View layer
 */
class ClientMyDataViewModel : AbstractModifyItemViewModel() {
    override val logic = ClientMyDataLogic()
    override var lowestRole = Role.CUSTOMER.ordinal

    private val _item: MutableLiveData<User> = MutableLiveData()
    val item: LiveData<User> = _item

    override val splitDataObject get() = NullableSplitDataObject(itemId, item.value?.basic, item.value?.details)

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<UserBasic>() ?: UserBasic()
        val details = snapshotsPair.details?.toObject<UserDetails>() ?: UserDetails()
        _item.value = User(itemId, basic, details)

        toolbarType.value = ToolbarType.SAVE
    }
}
package pi.restaurantapp.viewmodels.fragments.management.customers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.management.customers.PreviewCustomerLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.user.User
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.data.user.UserDetails
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for PreviewCustomerFragment.
 * @see pi.restaurantapp.logic.fragments.management.customers.PreviewCustomerLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.customers.PreviewCustomerFragment View layer
 */
class PreviewCustomerViewModel : AbstractPreviewItemViewModel() {
    override val logic = PreviewCustomerLogic()

    private val _item: MutableLiveData<User> = MutableLiveData()
    val item: LiveData<User> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        editable = false

        val basic = snapshotsPair.basic?.toObject<UserBasic>() ?: UserBasic()
        val details = snapshotsPair.details?.toObject<UserDetails>() ?: UserDetails()
        _item.value = User(itemId, basic, details)

        setReadyToUnlock()
    }

    override fun isDisabled(): Boolean {
        return item.value?.basic?.disabled == true
    }
}
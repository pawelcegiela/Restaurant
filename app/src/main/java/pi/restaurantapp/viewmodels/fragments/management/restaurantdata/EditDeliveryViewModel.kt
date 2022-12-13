package pi.restaurantapp.viewmodels.fragments.management.restaurantdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.management.restaurantdata.EditDeliveryLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.NullableSplitDataObject
import pi.restaurantapp.objects.data.delivery.Delivery
import pi.restaurantapp.objects.data.delivery.DeliveryBasic
import pi.restaurantapp.objects.data.delivery.DeliveryDetails
import pi.restaurantapp.objects.enums.ToolbarType
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for EditDeliveryFragment.
 * @see pi.restaurantapp.logic.fragments.management.restaurantdata.EditDeliveryLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.restaurantdata.EditDeliveryFragment View layer
 */
class EditDeliveryViewModel : AbstractModifyItemViewModel() {
    override val logic = EditDeliveryLogic()

    private val _item: MutableLiveData<Delivery> = MutableLiveData()
    val item: LiveData<Delivery> = _item

    override val splitDataObject get() = NullableSplitDataObject(itemId, item.value?.basic, item.value?.details)

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<DeliveryBasic>() ?: DeliveryBasic()
        val details = snapshotsPair.details?.toObject<DeliveryDetails>() ?: DeliveryDetails()
        _item.value = Delivery(basic, details)

        toolbarType.value = ToolbarType.SAVE
    }
}
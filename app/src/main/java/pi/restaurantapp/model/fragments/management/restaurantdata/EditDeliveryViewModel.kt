package pi.restaurantapp.model.fragments.management.restaurantdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.model.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.delivery.Delivery
import pi.restaurantapp.objects.data.delivery.DeliveryBasic
import pi.restaurantapp.objects.data.delivery.DeliveryDetails

class EditDeliveryViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "restaurantData"

    private val _item: MutableLiveData<Delivery> = MutableLiveData()
    val item: LiveData<Delivery> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<DeliveryBasic>() ?: DeliveryBasic()
        val details = snapshotsPair.details?.toObject<DeliveryDetails>() ?: DeliveryDetails()
        _item.value = Delivery(basic, details)
    }
}
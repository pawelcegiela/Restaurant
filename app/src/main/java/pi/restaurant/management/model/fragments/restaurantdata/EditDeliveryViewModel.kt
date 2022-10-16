package pi.restaurant.management.model.fragments.restaurantdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.model.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.objects.SnapshotsPair
import pi.restaurant.management.objects.data.delivery.Delivery
import pi.restaurant.management.objects.data.delivery.DeliveryBasic
import pi.restaurant.management.objects.data.delivery.DeliveryDetails

class EditDeliveryViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "restaurantData"

    private val _item: MutableLiveData<Delivery> = MutableLiveData()
    val item: LiveData<Delivery> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.getValue<DeliveryBasic>() ?: DeliveryBasic()
        val details = snapshotsPair.details?.getValue<DeliveryDetails>() ?: DeliveryDetails()
        _item.value = Delivery(basic, details)
    }
}
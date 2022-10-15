package pi.restaurant.management.model.fragments.restaurantdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.model.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.objects.SnapshotsPair
import pi.restaurant.management.objects.data.address.Address
import pi.restaurant.management.objects.data.address.AddressBasic
import pi.restaurant.management.objects.data.address.AddressDetails

class EditLocationViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "restaurantData"

    private val _item: MutableLiveData<Address> = MutableLiveData()
    val item: LiveData<Address> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.getValue<AddressBasic>() ?: AddressBasic()
        val details = snapshotsPair.details?.getValue<AddressDetails>() ?: AddressDetails()
        _item.value = Address(basic, details)
    }
}
package pi.restaurantapp.viewmodels.fragments.management.restaurantdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.management.restaurantdata.EditLocationLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.address.Address
import pi.restaurantapp.objects.data.address.AddressBasic
import pi.restaurantapp.objects.data.address.AddressDetails
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel

class EditLocationViewModel : AbstractModifyItemViewModel() {
    override val logic = EditLocationLogic()

    private val _item: MutableLiveData<Address> = MutableLiveData()
    val item: LiveData<Address> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<AddressBasic>() ?: AddressBasic()
        val details = snapshotsPair.details?.toObject<AddressDetails>() ?: AddressDetails()
        _item.value = Address(basic, details)
    }
}
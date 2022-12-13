package pi.restaurantapp.viewmodels.fragments.management.restaurantdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import com.mapbox.geojson.Point
import pi.restaurantapp.logic.fragments.management.restaurantdata.EditLocationLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.NullableSplitDataObject
import pi.restaurantapp.objects.data.address.Address
import pi.restaurantapp.objects.data.address.AddressBasic
import pi.restaurantapp.objects.data.address.AddressDetails
import pi.restaurantapp.objects.enums.ToolbarType
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for EditLocationFragment.
 * @see pi.restaurantapp.logic.fragments.management.restaurantdata.EditLocationLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.restaurantdata.EditLocationFragment View layer
 */
class EditLocationViewModel : AbstractModifyItemViewModel() {
    override val logic = EditLocationLogic()

    private val _item: MutableLiveData<Address> = MutableLiveData()
    val item: LiveData<Address> = _item

    var point = MutableLiveData<Point>()

    override val splitDataObject get() = NullableSplitDataObject(itemId, item.value?.basic, item.value?.details)

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<AddressBasic>() ?: AddressBasic()
        val details = snapshotsPair.details?.toObject<AddressDetails>() ?: AddressDetails()
        _item.value = Address(basic, details)
        if (basic.longitude != null && basic.latitude != null) {
            point.value = Point.fromLngLat(basic.longitude!!, basic.latitude!!)
        }

        toolbarType.value = ToolbarType.SAVE
    }

    fun setPoint(newPoint: Point) {
        point.value = newPoint
        item.value?.basic?.latitude = newPoint.latitude()
        item.value?.basic?.longitude = newPoint.longitude()
    }
}
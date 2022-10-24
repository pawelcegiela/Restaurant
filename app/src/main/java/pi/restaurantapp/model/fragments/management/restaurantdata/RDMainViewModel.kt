package pi.restaurantapp.model.fragments.management.restaurantdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ktx.getValue
import pi.restaurantapp.model.fragments.management.AbstractPreviewItemViewModel
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.restaurantdata.RestaurantData
import pi.restaurantapp.objects.data.restaurantdata.RestaurantDataBasic
import pi.restaurantapp.objects.data.restaurantdata.RestaurantDataDetails

class RDMainViewModel : AbstractPreviewItemViewModel() {
    override val databasePath = "restaurantData"

    private val _item: MutableLiveData<RestaurantData> = MutableLiveData()
    val item: LiveData<RestaurantData> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.getValue<RestaurantDataBasic>() ?: RestaurantDataBasic()
        val details = snapshotsPair.details?.getValue<RestaurantDataDetails>() ?: RestaurantDataDetails()
        _item.value = RestaurantData(basic, details)
    }

    override fun isDisabled(): Boolean {
        return false
    }
}
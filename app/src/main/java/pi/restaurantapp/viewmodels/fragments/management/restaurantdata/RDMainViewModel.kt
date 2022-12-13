package pi.restaurantapp.viewmodels.fragments.management.restaurantdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.logic.fragments.management.restaurantdata.RDMainLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.restaurantdata.RestaurantData
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for RDMainFragment.
 * @see pi.restaurantapp.logic.fragments.management.restaurantdata.RDMainLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.restaurantdata.RDMainFragment View layer
 */
class RDMainViewModel : AbstractPreviewItemViewModel() {
    override val logic = RDMainLogic()

    private val _item: MutableLiveData<RestaurantData> = MutableLiveData()
    val item: LiveData<RestaurantData> = _item

    override fun getDataFromDatabase() {
        logic.getDataFromDatabase { restaurantData: RestaurantData ->
            _item.value = restaurantData
            setReadyToInitialize()
        }
    }

    override fun getItem(snapshotsPair: SnapshotsPair) {}

    override fun isDisabled(): Boolean {
        return false
    }
}
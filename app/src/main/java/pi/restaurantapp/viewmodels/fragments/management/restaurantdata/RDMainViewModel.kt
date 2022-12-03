package pi.restaurantapp.viewmodels.fragments.management.restaurantdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.aboutrestaurant.AboutRestaurantBasic
import pi.restaurantapp.objects.data.address.AddressBasic
import pi.restaurantapp.objects.data.delivery.DeliveryBasic
import pi.restaurantapp.objects.data.openinghours.OpeningHoursBasic
import pi.restaurantapp.objects.data.restaurantdata.RestaurantData
import pi.restaurantapp.objects.data.restaurantdata.RestaurantDataBasic
import pi.restaurantapp.objects.data.restaurantdata.RestaurantDataDetails

class RDMainViewModel : AbstractPreviewItemViewModel() {
    override val databasePath = "restaurantData"

    private val _item: MutableLiveData<RestaurantData> = MutableLiveData()
    val item: LiveData<RestaurantData> = _item

    override fun getDataFromDatabase() {
        dbRefBasic.get().addOnSuccessListener { snapshot ->
            val dataBasic = RestaurantDataBasic()
            for (snapshotDocument in snapshot.documents) {
                when (snapshotDocument.id) {
                    "aboutRestaurant" -> dataBasic.aboutRestaurant = snapshotDocument.toObject() ?: AboutRestaurantBasic()
                    "openingHours" -> dataBasic.openingHours = snapshotDocument.toObject() ?: OpeningHoursBasic()
                    "location" -> dataBasic.location = snapshotDocument.toObject() ?: AddressBasic()
                    "delivery" -> dataBasic.delivery = snapshotDocument.toObject() ?: DeliveryBasic()
                }
            }
            _item.value = RestaurantData(dataBasic, RestaurantDataDetails())
            setReadyToInitialize()
        }
    }

    override fun getItem(snapshotsPair: SnapshotsPair) {}

    override fun isDisabled(): Boolean {
        return false
    }
}
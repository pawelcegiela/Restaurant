package pi.restaurantapp.logic.fragments.management.restaurantdata

import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.AbstractPreviewItemLogic
import pi.restaurantapp.objects.data.aboutrestaurant.AboutRestaurantBasic
import pi.restaurantapp.objects.data.address.AddressBasic
import pi.restaurantapp.objects.data.delivery.DeliveryBasic
import pi.restaurantapp.objects.data.openinghours.OpeningHoursBasic
import pi.restaurantapp.objects.data.restaurantdata.RestaurantData
import pi.restaurantapp.objects.data.restaurantdata.RestaurantDataBasic
import pi.restaurantapp.objects.data.restaurantdata.RestaurantDataDetails

/**
 * Class responsible for business logic and communication with database (Model layer) for RDMainFragment.
 * @see pi.restaurantapp.ui.fragments.management.restaurantdata.RDMainFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.restaurantdata.RDMainViewModel ViewModel layer
 */
class RDMainLogic : AbstractPreviewItemLogic() {
    override val databasePath = "restaurantData"

    fun getDataFromDatabase(callback: (RestaurantData) -> Unit) {
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
            callback(RestaurantData(dataBasic, RestaurantDataDetails()))
        }
    }
}
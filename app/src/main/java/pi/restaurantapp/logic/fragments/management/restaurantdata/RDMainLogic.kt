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
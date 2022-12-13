package pi.restaurantapp.objects.data.restaurantdata

import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.aboutrestaurant.AboutRestaurantBasic
import pi.restaurantapp.objects.data.address.AddressBasic
import pi.restaurantapp.objects.data.delivery.DeliveryBasic
import pi.restaurantapp.objects.data.openinghours.OpeningHoursBasic

/**
 * Data class containing basic information of RestaurantData.
 * @see pi.restaurantapp.objects.data.restaurantdata.RestaurantData
 */
class RestaurantDataBasic : AbstractDataObject() {
    var aboutRestaurant: AboutRestaurantBasic = AboutRestaurantBasic()
    var openingHours: OpeningHoursBasic = OpeningHoursBasic()
    var location: AddressBasic = AddressBasic()
    var delivery: DeliveryBasic = DeliveryBasic()

}
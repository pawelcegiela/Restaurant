package pi.restaurantapp.objects.data.restaurantdata

import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.aboutrestaurant.AboutRestaurantBasic
import pi.restaurantapp.objects.data.address.AddressBasic
import pi.restaurantapp.objects.data.delivery.DeliveryBasic
import pi.restaurantapp.objects.data.openinghours.OpeningHoursBasic

class RestaurantDataBasic : AbstractDataObject() {
    lateinit var aboutRestaurant: AboutRestaurantBasic
    lateinit var openingHours: OpeningHoursBasic
    lateinit var location: AddressBasic
    lateinit var delivery: DeliveryBasic

}
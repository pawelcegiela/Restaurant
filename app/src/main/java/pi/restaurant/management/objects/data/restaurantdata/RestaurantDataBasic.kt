package pi.restaurant.management.objects.data.restaurantdata

import pi.restaurant.management.objects.data.AbstractDataObject
import pi.restaurant.management.objects.data.aboutrestaurant.AboutRestaurantBasic
import pi.restaurant.management.objects.data.address.AddressBasic
import pi.restaurant.management.objects.data.openinghours.OpeningHoursBasic

class RestaurantDataBasic() : AbstractDataObject() {
    lateinit var aboutRestaurant: AboutRestaurantBasic
    lateinit var openingHours: OpeningHoursBasic
    lateinit var location: AddressBasic
    // TODO delivery

}
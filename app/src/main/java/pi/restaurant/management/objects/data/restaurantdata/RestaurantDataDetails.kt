package pi.restaurant.management.objects.data.restaurantdata

import pi.restaurant.management.objects.data.AbstractDataObject
import pi.restaurant.management.objects.data.aboutrestaurant.AboutRestaurantBasic
import pi.restaurant.management.objects.data.aboutrestaurant.AboutRestaurantDetails
import pi.restaurant.management.objects.data.address.AddressBasic
import pi.restaurant.management.objects.data.address.AddressDetails
import pi.restaurant.management.objects.data.openinghours.OpeningHoursBasic
import pi.restaurant.management.objects.data.openinghours.OpeningHoursDetails
import pi.restaurant.management.utils.StringFormatUtils

class RestaurantDataDetails() : AbstractDataObject() {
    lateinit var aboutRestaurant: AboutRestaurantDetails
    lateinit var openingHours: OpeningHoursDetails
    lateinit var location: AddressDetails
}
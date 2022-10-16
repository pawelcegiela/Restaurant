package pi.restaurant.management.objects.data.restaurantdata

import pi.restaurant.management.objects.data.AbstractDataObject
import pi.restaurant.management.objects.data.aboutrestaurant.AboutRestaurantDetails
import pi.restaurant.management.objects.data.address.AddressDetails
import pi.restaurant.management.objects.data.delivery.DeliveryDetails
import pi.restaurant.management.objects.data.openinghours.OpeningHoursDetails

class RestaurantDataDetails() : AbstractDataObject() {
    lateinit var aboutRestaurant: AboutRestaurantDetails
    lateinit var openingHours: OpeningHoursDetails
    lateinit var location: AddressDetails
    lateinit var delivery: DeliveryDetails
}
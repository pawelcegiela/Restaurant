package pi.restaurantapp.objects.data.restaurantdata

import pi.restaurantapp.objects.data.AbstractDataObject

/**
 * Data class containing id, basic information and details for restaurant data.
 */
class RestaurantData(var basic: RestaurantDataBasic, var details: RestaurantDataDetails) : AbstractDataObject()
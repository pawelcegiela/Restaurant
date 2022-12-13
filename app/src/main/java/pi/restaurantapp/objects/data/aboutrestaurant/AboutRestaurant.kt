package pi.restaurantapp.objects.data.aboutrestaurant

import pi.restaurantapp.objects.data.AbstractDataObject

/**
 * Data class containing id, basic information and details for restaurant details.
 */
class AboutRestaurant : AbstractDataObject {
    override var id = "aboutRestaurant"
    lateinit var basic: AboutRestaurantBasic
    lateinit var details: AboutRestaurantDetails

    @Suppress("unused")
    constructor()

    constructor(basic: AboutRestaurantBasic, details: AboutRestaurantDetails) {
        this.basic = basic
        this.details = details
    }
}
package pi.restaurant.management.objects.data.aboutrestaurant

import pi.restaurant.management.objects.data.AbstractDataObject

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
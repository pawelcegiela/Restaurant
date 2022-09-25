package pi.restaurant.management.data

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
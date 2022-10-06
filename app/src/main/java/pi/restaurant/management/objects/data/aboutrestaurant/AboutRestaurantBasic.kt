package pi.restaurant.management.objects.data.aboutrestaurant

import pi.restaurant.management.objects.data.AbstractDataObject

class AboutRestaurantBasic : AbstractDataObject {
    override var id = "aboutRestaurant"
    var name: String = ""
    var description: String = ""

    @Suppress("unused")
    constructor()

    constructor(name: String, description: String) {
        this.name = name
        this.description = description
    }
}
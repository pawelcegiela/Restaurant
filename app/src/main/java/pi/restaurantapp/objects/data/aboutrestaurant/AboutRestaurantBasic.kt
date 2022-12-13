package pi.restaurantapp.objects.data.aboutrestaurant

import pi.restaurantapp.objects.data.AbstractDataObject

/**
 * Data class containing basic information of AboutRestaurant.
 * @see pi.restaurantapp.objects.data.aboutrestaurant.AboutRestaurant
 */
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
package pi.restaurantapp.objects.data.openinghours

import pi.restaurantapp.objects.data.AbstractDataObject

/**
 * Data class containing id, basic information and details for opening hours.
 */
class OpeningHours : AbstractDataObject {
    override var id = "openingHours"
    var basic: OpeningHoursBasic = OpeningHoursBasic()
    var details: OpeningHoursDetails = OpeningHoursDetails()

    @Suppress("unused")
    constructor()

    constructor(basic: OpeningHoursBasic, details: OpeningHoursDetails) {
        this.basic = basic
        this.details = details
    }
}
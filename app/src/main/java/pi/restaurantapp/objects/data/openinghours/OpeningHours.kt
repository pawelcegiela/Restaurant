package pi.restaurantapp.objects.data.openinghours

import pi.restaurantapp.objects.data.AbstractDataObject

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
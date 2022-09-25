package pi.restaurant.management.data

class Location : AbstractDataObject {
    override var id = "location"
    lateinit var basic: LocationBasic
    lateinit var details: LocationDetails

    @Suppress("unused")
    constructor()

    constructor(basic: LocationBasic, details: LocationDetails) {
        this.basic = basic
        this.details = details
    }
}
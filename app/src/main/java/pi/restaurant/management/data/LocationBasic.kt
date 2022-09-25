package pi.restaurant.management.data

class LocationBasic : AbstractDataObject {
    override var id = "location"
    var city = ""
    var postalCode = ""
    var street = ""
    var houseNumber = ""
    var flatNumber = ""
    var latitude = ""
    var longitude = ""

    @Suppress("unused")
    constructor()

    constructor(city: String, postalCode: String, street: String, houseNumber: String, flatNumber: String) {
        this.city = city
        this.postalCode = postalCode
        this.street = street
        this.houseNumber = houseNumber
        this.flatNumber = flatNumber
    }
}
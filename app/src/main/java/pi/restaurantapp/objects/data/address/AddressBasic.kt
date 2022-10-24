package pi.restaurantapp.objects.data.address

import pi.restaurantapp.objects.data.AbstractDataObject

class AddressBasic : AbstractDataObject {
    override var id = "location"
    var city = ""
    var postalCode = ""
    var street = ""
    var houseNumber = ""
    var flatNumber = ""

    @Suppress("unused")
    constructor()

    constructor(city: String, postalCode: String, street: String, houseNumber: String, flatNumber: String) {
        this.city = city
        this.postalCode = postalCode
        this.street = street
        this.houseNumber = houseNumber
        this.flatNumber = flatNumber
    }

    constructor(
        id: String,
        city: String,
        postalCode: String,
        street: String,
        houseNumber: String,
        flatNumber: String
    ) : this(city, postalCode, street, houseNumber, flatNumber) {
        this.id = id
    }
}
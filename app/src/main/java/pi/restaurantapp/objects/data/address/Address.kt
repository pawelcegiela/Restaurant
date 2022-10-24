package pi.restaurantapp.objects.data.address

import pi.restaurantapp.objects.data.AbstractDataObject

class Address : AbstractDataObject {
    override var id = "location"
    lateinit var basic: AddressBasic
    lateinit var details: AddressDetails

    @Suppress("unused")
    constructor()

    constructor(basic: AddressBasic, details: AddressDetails) {
        this.basic = basic
        this.details = details
    }
}
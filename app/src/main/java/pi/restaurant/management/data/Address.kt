package pi.restaurant.management.data

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
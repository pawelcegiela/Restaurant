package pi.restaurantapp.objects.data.delivery

import pi.restaurantapp.objects.data.AbstractDataObject

class Delivery : AbstractDataObject {
    override var id = "delivery"
    lateinit var basic: DeliveryBasic
    lateinit var details: DeliveryDetails

    @Suppress("unused")
    constructor()

    constructor(basic: DeliveryBasic, details: DeliveryDetails) {
        this.basic = basic
        this.details = details
    }
}
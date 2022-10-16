package pi.restaurant.management.objects.data.delivery

import pi.restaurant.management.objects.data.AbstractDataObject

class DeliveryBasic : AbstractDataObject {
    override var id = "delivery"
    var available: Boolean = false
    var minimumPrice: String = "0.0"
    var extraDeliveryFee: String = "0.0"
    var freeDeliveryAvailable: Boolean = false
    var minimumPriceFreeDelivery: String = "0.0"

    @Suppress("unused")
    constructor()

    constructor(available: Boolean) {
        this.available = available
    }

    constructor(
        available: Boolean,
        minimumPrice: String,
        extraDeliveryFee: String,
        freeDeliveryAvailable: Boolean,
        minimumPriceFreeDelivery: String
    ) {
        this.available = available
        this.minimumPrice = minimumPrice
        this.extraDeliveryFee = extraDeliveryFee
        this.freeDeliveryAvailable = freeDeliveryAvailable
        this.minimumPriceFreeDelivery = minimumPriceFreeDelivery
    }
}
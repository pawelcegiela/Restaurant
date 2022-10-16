package pi.restaurant.management.objects.data.delivery

import pi.restaurant.management.objects.data.AbstractDataObject

class DeliveryBasic : AbstractDataObject {
    override var id = "delivery"
    var available: Boolean = false
    var minimumPrice: Double = 0.0
    var extraDeliveryFee: Double = 0.0
    var freeDeliveryAvailable: Boolean = false
    var minimumPriceFreeDelivery: Double = 0.0

    @Suppress("unused")
    constructor()

    constructor(available: Boolean) {
        this.available = available
    }

    constructor(
        available: Boolean,
        minimumPrice: Double,
        extraDeliveryFee: Double,
        freeDeliveryAvailable: Boolean,
        minimumPriceFreeDelivery: Double
    ) {
        this.available = available
        this.minimumPrice = minimumPrice
        this.extraDeliveryFee = extraDeliveryFee
        this.freeDeliveryAvailable = freeDeliveryAvailable
        this.minimumPriceFreeDelivery = minimumPriceFreeDelivery
    }
}
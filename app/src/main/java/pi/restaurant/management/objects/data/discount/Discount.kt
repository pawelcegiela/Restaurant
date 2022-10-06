package pi.restaurant.management.objects.data.discount

import pi.restaurant.management.objects.data.AbstractDataObject


class Discount : AbstractDataObject {
    lateinit var basic: DiscountBasic
    lateinit var details: DiscountDetails

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        basic: DiscountBasic,
        details: DiscountDetails
    ) {
        this.id = id
        this.basic = basic
        this.details = details
    }
}
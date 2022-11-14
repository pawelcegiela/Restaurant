package pi.restaurantapp.objects.data.discount

import pi.restaurantapp.objects.data.AbstractDataObject


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

    fun onIdChanged(s: CharSequence) {
        this.id = s.toString()
        this.basic.id = s.toString()
        this.details.id = s.toString()
    }
}
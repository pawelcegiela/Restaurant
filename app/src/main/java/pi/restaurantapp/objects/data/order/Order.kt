package pi.restaurantapp.objects.data.order

import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.utils.StringFormatUtils

class Order : AbstractDataObject {
    lateinit var basic: OrderBasic
    lateinit var details: OrderDetails

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        basic: OrderBasic,
        details: OrderDetails
    ) {
        this.id = id.ifEmpty { StringFormatUtils.formatId() }
        this.basic = basic
        this.details = details
    }
}
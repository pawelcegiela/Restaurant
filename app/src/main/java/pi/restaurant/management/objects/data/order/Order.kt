package pi.restaurant.management.objects.data.order

import pi.restaurant.management.objects.data.AbstractDataObject
import pi.restaurant.management.utils.StringFormatUtils

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
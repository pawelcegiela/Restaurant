package pi.restaurantapp.objects.data.order

import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.AbstractDataObject

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
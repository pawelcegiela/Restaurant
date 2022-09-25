package pi.restaurant.management.data

import pi.restaurant.management.utils.StringFormatUtils
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

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
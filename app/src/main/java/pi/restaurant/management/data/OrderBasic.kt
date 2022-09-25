package pi.restaurant.management.data

import pi.restaurant.management.utils.StringFormatUtils
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class OrderBasic : AbstractDataObject {
    var orderStatus: Int = 0
    var collectionDate: Date = Date()
    var deliveryType: Int = 0
    var value: Double = 0.0
    var name: String = ""

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        orderStatus: Int,
        collectionDate: Date,
        deliveryType: Int,
        value: Double,
        name: String,
    ) {
        this.id = id.ifEmpty { StringFormatUtils.formatId() }
        this.orderStatus = orderStatus
        this.collectionDate = collectionDate
        this.deliveryType = deliveryType
        this.value = value
        this.name = name
    }
}
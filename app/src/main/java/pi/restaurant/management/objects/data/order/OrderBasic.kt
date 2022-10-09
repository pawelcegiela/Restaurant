package pi.restaurant.management.objects.data.order

import pi.restaurant.management.objects.data.AbstractDataObject
import pi.restaurant.management.utils.StringFormatUtils
import java.util.*

class OrderBasic : AbstractDataObject {
    var orderStatus: Int = 0
    var collectionDate: Date = Date()
    var collectionType: Int = 0
    var value: Double = 0.0
    var name: String = ""

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        orderStatus: Int,
        collectionDate: Date,
        collectionType: Int,
        value: Double,
        name: String,
    ) {
        this.id = id.ifEmpty { StringFormatUtils.formatId() }
        this.orderStatus = orderStatus
        this.collectionDate = collectionDate
        this.collectionType = collectionType
        this.value = value
        this.name = name
    }
}
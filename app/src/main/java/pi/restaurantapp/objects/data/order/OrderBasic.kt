package pi.restaurantapp.objects.data.order

import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.utils.StringFormatUtils
import java.util.*

class OrderBasic : AbstractDataObject {
    var orderStatus: Int = 0
    var collectionDate: Date = Date()
    var collectionType: Int = 0
    var value: String = "0.0"
    var name: String = ""
    var userId: String = ""
    var disabled: Boolean = false

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        orderStatus: Int,
        collectionDate: Date,
        collectionType: Int,
        value: String,
        name: String,
        userId: String
    ) {
        this.id = id.ifEmpty { StringFormatUtils.formatId() }
        this.orderStatus = orderStatus
        this.collectionDate = collectionDate
        this.collectionType = collectionType
        this.value = value
        this.name = name
        this.userId = userId
    }

    constructor(id: String, userId: String) {
        this.id = id
        this.userId = userId
    }

}
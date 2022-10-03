package pi.restaurant.management.data

import pi.restaurant.management.utils.StringFormatUtils
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class OrderDetails : AbstractDataObject {
    var userId: String = ""
    var orderType: Int = 0
    var orderDate: Date = Date()
    var orderPlace: Int = 0
    var dishes: HashMap<String, DishItem> = HashMap()
    var address : AddressBasic? = null

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        userId: String,
        orderType: Int,
        orderDate: Date,
        orderPlace: Int,
        dishes: HashMap<String, DishItem>,
        address: AddressBasic?
    ) {
        this.id = id.ifEmpty { StringFormatUtils.formatId() }
        this.userId = userId
        this.orderType = orderType
        this.orderDate = orderDate
        this.orderPlace = orderPlace
        this.dishes = dishes
        this.address = address
    }
}
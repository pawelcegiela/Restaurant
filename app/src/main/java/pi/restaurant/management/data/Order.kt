package pi.restaurant.management.data

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Order : AbstractDataObject {
    var userId: String = ""
    var orderType: Int = 0
    var orderStatus: Int = 0
    var orderDate: Date = Date()
    var collectionDate: Date = Date()
    var deliveryType: Int = 0
    var orderPlace: Int = 0
    var dishes: HashMap<String, DishItem> = HashMap()
    var value: Double = 0.0
    var name: String = ""

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        userId: String,
        orderType: Int,
        orderStatus: Int,
        orderDate: Date,
        collectionDate: Date,
        deliveryType: Int,
        orderPlace: Int,
        dishes: HashMap<String, DishItem>,
        value: Double,
        name: String,
    ) {
        if (id.isEmpty()) {
            this.id =
                name.trim().replace(" ", "_") + "_" + Date().time + "_" + Random().nextInt(1000)
        } else {
            this.id = id
        }
        this.userId = userId
        this.orderType = orderType
        this.orderStatus = orderStatus
        this.orderDate = orderDate
        this.collectionDate = collectionDate
        this.deliveryType = deliveryType
        this.orderPlace = orderPlace
        this.dishes = dishes
        this.value = value
        this.name = name
    }
}
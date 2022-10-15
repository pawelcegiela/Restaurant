package pi.restaurant.management.objects.data.order

import pi.restaurant.management.objects.data.AbstractDataObject
import pi.restaurant.management.objects.data.address.AddressBasic
import pi.restaurant.management.objects.data.dish.DishItem
import pi.restaurant.management.utils.StringFormatUtils
import java.util.*

class OrderDetails : AbstractDataObject {
    var userId: String = ""
    var orderType: Int = 0
    var orderDate: Date = Date()
    var modificationDate: Date = Date()
    var orderPlace: Int = 0
    var dishes: HashMap<String, DishItem> = HashMap()
    var address : AddressBasic? = null
    var statusChanges: HashMap<String, Int> = HashMap()
    var delivererId: String = ""
    var contactPhone: String = ""

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        userId: String,
        orderType: Int,
        orderDate: Date,
        modificationDate: Date,
        orderPlace: Int,
        dishes: HashMap<String, DishItem>,
        address: AddressBasic?,
        statusChanges: HashMap<String, Int>,
        delivererId: String,
        contactPhone: String
    ) {
        this.id = id.ifEmpty { StringFormatUtils.formatId() }
        this.userId = userId
        this.orderType = orderType
        this.orderDate = orderDate
        this.modificationDate = modificationDate
        this.orderPlace = orderPlace
        this.dishes = dishes
        this.address = address
        this.statusChanges = statusChanges
        this.delivererId = delivererId
        this.contactPhone = contactPhone
    }
}
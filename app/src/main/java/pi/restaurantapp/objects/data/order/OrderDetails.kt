package pi.restaurantapp.objects.data.order

import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.address.AddressBasic
import pi.restaurantapp.objects.data.dish.DishItem
import pi.restaurantapp.utils.StringFormatUtils
import java.util.*

class OrderDetails : AbstractDataObject {
    var orderType: Int = 0
    var orderDate: Date = Date()
    var modificationDate: Date = Date()
    var orderPlace: Int = 0
    var dishes: HashMap<String, DishItem> = HashMap()
    var address : AddressBasic = AddressBasic()
    var statusChanges: HashMap<String, Int> = HashMap()
    var delivererId: String = ""
    var contactPhone: String = ""
    var comments: String = ""
    var discount: String = ""

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        orderType: Int,
        orderDate: Date,
        modificationDate: Date,
        orderPlace: Int,
        dishes: HashMap<String, DishItem>,
        address: AddressBasic,
        statusChanges: HashMap<String, Int>,
        delivererId: String,
        contactPhone: String,
        comments: String
    ) {
        this.id = id.ifEmpty { StringFormatUtils.formatId() }
        this.orderType = orderType
        this.orderDate = orderDate
        this.modificationDate = modificationDate
        this.orderPlace = orderPlace
        this.dishes = dishes
        this.address = address
        this.statusChanges = statusChanges
        this.delivererId = delivererId
        this.contactPhone = contactPhone
        this.comments = comments
    }

    constructor(id: String) {
        this.id = id
    }
}
package pi.restaurantapp.objects.data.user

import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.address.AddressBasic
import pi.restaurantapp.objects.enums.CollectionType
import pi.restaurantapp.objects.enums.OrderPlace
import java.util.*

class UserDetails : AbstractDataObject {
    var email: String = ""
    var creationDate: Date = Date()
    var ordersToDeliver = HashMap<String, Boolean>()
    var defaultDeliveryAddress = AddressBasic()
    var contactPhone: String = ""
    var preferredCollectionType: Int = CollectionType.DELIVERY.ordinal
    var preferredOrderPlace: Int = OrderPlace.TO_GO.ordinal

    @Suppress("unused")
    constructor() {
        this.id = ""
    }

    constructor(
        id: String, email: String, creationDate: Date
    ) {
        this.id = id
        this.email = email
        this.creationDate = creationDate
    }

    constructor(id: String, email: String, creationDate: Date, ordersToDeliver: HashMap<String, Boolean>) {
        this.id = id
        this.email = email
        this.creationDate = creationDate
        this.ordersToDeliver = ordersToDeliver
    }

    constructor(
        id: String,
        email: String,
        creationDate: Date,
        defaultDeliveryAddress: AddressBasic,
        contactPhone: String,
        preferredCollectionType: Int,
        preferredOrderPlace: Int
    ) {
        this.id = id
        this.email = email
        this.creationDate = creationDate
        this.defaultDeliveryAddress = defaultDeliveryAddress
        this.contactPhone = contactPhone
        this.preferredCollectionType = preferredCollectionType
        this.preferredOrderPlace = preferredOrderPlace
    }
}
package pi.restaurantapp.objects.data.user

import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.address.AddressBasic
import java.util.*

class UserDetails : AbstractDataObject {
    var email: String = ""
    var creationDate: Date = Date()
    var ordersToDeliver = HashMap<String, Boolean>()
    var defaultDeliveryAddress = AddressBasic()
    var contactPhone: String = ""

    @Suppress("unused")
    constructor() {
        this.id = ""
    }

    constructor(
        id: String,
        email: String,
        creationDate: Date,
        ordersToDeliver: HashMap<String, Boolean>,
        defaultDeliveryAddress: AddressBasic,
        contactPhone: String
    ) {
        this.id = id
        this.email = email
        this.creationDate = creationDate
        this.ordersToDeliver = ordersToDeliver
        this.defaultDeliveryAddress = defaultDeliveryAddress
        this.contactPhone = contactPhone
    }
}
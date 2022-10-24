package pi.restaurantapp.objects.data.user

import pi.restaurantapp.objects.data.AbstractDataObject
import java.util.*

class UserDetails : AbstractDataObject {
    var email: String = ""
    var creationDate: Date = Date()
    var ordersToDeliver = HashMap<String, Boolean>()

    @Suppress("unused")
    constructor() {
        this.id = ""
    }

    constructor(id: String, email: String, creationDate: Date, ordersToDeliver: HashMap<String, Boolean>) {
        this.id = id
        this.email = email
        this.creationDate = creationDate
        this.ordersToDeliver = ordersToDeliver
    }
}
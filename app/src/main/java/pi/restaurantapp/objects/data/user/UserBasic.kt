package pi.restaurantapp.objects.data.user

import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.enums.Role

/**
 * Data class containing basic information of AboutRestaurant.
 * @see pi.restaurantapp.objects.data.user.User
 */
class UserBasic : AbstractDataObject {
    var firstName = ""
    var lastName = ""
    var role = Role.WORKER.ordinal
    var disabled: Boolean = false
    var delivery: Boolean = false

    @Suppress("unused")
    constructor() {
        this.id = ""
    }

    constructor(id: String, firstName: String, lastName: String, role: Int, disabled: Boolean, delivery: Boolean) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.role = role
        this.disabled = disabled
        this.delivery = delivery
    }

    fun getFullName(): String {
        return "$firstName $lastName"
    }
}
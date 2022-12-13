package pi.restaurantapp.objects.data.user

import pi.restaurantapp.objects.data.AbstractDataObject

/**
 * Data class containing id, basic information and details for user.
 */
class User : AbstractDataObject {
    lateinit var basic: UserBasic
    lateinit var details: UserDetails

    @Suppress("unused")
    constructor() {
        this.id = ""
    }

    constructor(id: String, basic: UserBasic, details: UserDetails) {
        this.id = id
        this.basic = basic
        this.details = details
    }
}
package pi.restaurant.management.objects.data.user

import pi.restaurant.management.objects.data.AbstractDataObject

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
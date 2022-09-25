package pi.restaurant.management.data

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
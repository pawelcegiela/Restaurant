package pi.restaurant.management.data

class UserDetails : AbstractDataObject {
    var email: String = ""

    @Suppress("unused")
    constructor() {
        this.id = ""
    }

    constructor(id: String, email: String) {
        this.id = id
        this.email = email
    }
}
package pi.restaurant.management.data

import java.util.*

class UserDetails : AbstractDataObject {
    var email: String = ""
    var creationDate: Date = Date()

    @Suppress("unused")
    constructor() {
        this.id = ""
    }

    constructor(id: String, email: String, creationDate: Date) {
        this.id = id
        this.email = email
        this.creationDate = creationDate
    }
}
package pi.restaurant.management.data

class UserBasic : AbstractDataObject {
    var firstName = ""
    var lastName = ""
    var role = 3
    var disabled: Boolean = false

    @Suppress("unused")
    constructor() {
        this.id = ""
    }

    constructor(id: String, firstName: String, lastName: String, role: Int, disabled: Boolean) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.role = role
        this.disabled = disabled
    }
}
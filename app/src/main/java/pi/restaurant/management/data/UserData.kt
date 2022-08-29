package pi.restaurant.management.data

class UserData : AbstractDataObject {
    var firstName = ""
    var lastName = ""
    var email: String = ""
    var role = 3
    var disabled: Boolean = false

    @Suppress("unused")
    constructor() {
        this.id = ""
    }

    constructor(id: String, firstName: String, lastName: String, email: String, role: Int, disabled: Boolean) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.role = role
        this.disabled = disabled
    }
}
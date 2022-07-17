package pi.restaurant.management.data

class UserData {
    var id: String = ""
    var firstName = ""
    var lastName = ""
    var email: String = ""
    var role = 3
    var disabled: Boolean = false


    // Firebase constructor
    constructor()

    constructor(id: String, firstName: String, lastName: String, email: String, role: Int) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.role = role
    }
}
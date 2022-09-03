package pi.restaurant.management.data

class Location : AbstractDataObject() {
    override var id = "location"
    var city = ""
    var postalCode = ""
    var street = ""
    var houseNumber = ""
    var flatNumber = ""
    var latitude = ""
    var longitude = ""
}
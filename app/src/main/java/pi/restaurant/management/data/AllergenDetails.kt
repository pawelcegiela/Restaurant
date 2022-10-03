package pi.restaurant.management.data

import pi.restaurant.management.utils.StringFormatUtils

class AllergenDetails : AbstractDataObject {
    var description: String = ""
    var containingDishes: HashMap<String, Boolean> = HashMap()

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        description: String
    ) {
        this.id = id.ifEmpty { StringFormatUtils.formatId() }
        this.description = description
    }
}
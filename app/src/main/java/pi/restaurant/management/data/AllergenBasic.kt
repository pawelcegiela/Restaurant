package pi.restaurant.management.data

import pi.restaurant.management.utils.StringFormatUtils

class AllergenBasic : AbstractDataObject {
    var name: String = ""

    @Suppress("unused")
    constructor()

    constructor(id: String, name: String) {
        this.id = id.ifEmpty { StringFormatUtils.formatId() }
        this.name = name
    }
}
package pi.restaurantapp.objects.data.allergen

import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.logic.utils.StringFormatUtils

class AllergenBasic : AbstractDataObject {
    var name: String = ""
    var disabled: Boolean = false

    @Suppress("unused")
    constructor()

    constructor(id: String, name: String) {
        this.id = id.ifEmpty { StringFormatUtils.formatId() }
        this.name = name
    }

    constructor(id: String) {
        this.id = id
    }
}
package pi.restaurant.management.objects.data.allergen

import pi.restaurant.management.objects.data.AbstractDataObject
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
package pi.restaurantapp.objects.data.allergen

import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.AbstractDataObject

/**
 * Data class containing basic information of Allergen.
 * @see pi.restaurantapp.objects.data.allergen.Allergen
 */
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
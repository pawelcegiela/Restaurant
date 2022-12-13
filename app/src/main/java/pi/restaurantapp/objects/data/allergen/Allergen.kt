package pi.restaurantapp.objects.data.allergen

import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.AbstractDataObject

/**
 * Data class containing id, basic information and details for allergen.
 */
class Allergen : AbstractDataObject {
    lateinit var basic: AllergenBasic
    lateinit var details: AllergenDetails

    @Suppress("unused")
    constructor()

    constructor(id: String, basic: AllergenBasic, details: AllergenDetails) {
        this.id = id.ifEmpty { StringFormatUtils.formatId() }
        this.basic = basic
        this.details = details
    }
}
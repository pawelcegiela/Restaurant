package pi.restaurant.management.objects.data.allergen

import pi.restaurant.management.objects.data.AbstractDataObject
import pi.restaurant.management.utils.StringFormatUtils

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
package pi.restaurantapp.objects.data.allergen

import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.logic.utils.StringFormatUtils

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
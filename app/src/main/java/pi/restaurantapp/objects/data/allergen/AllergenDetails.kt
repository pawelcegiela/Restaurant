package pi.restaurantapp.objects.data.allergen

import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.utils.StringFormatUtils

class AllergenDetails : AbstractDataObject {
    var description: String = ""
    var containingDishes: HashMap<String, Boolean> = HashMap()

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        description: String,
        containingDishes: HashMap<String, Boolean>
    ) {
        this.id = id.ifEmpty { StringFormatUtils.formatId() }
        this.description = description
        this.containingDishes = containingDishes
    }

    constructor(id: String) {
        this.id = id
    }
}
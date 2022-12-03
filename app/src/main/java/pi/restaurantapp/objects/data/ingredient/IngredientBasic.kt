package pi.restaurantapp.objects.data.ingredient

import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.logic.utils.StringFormatUtils

class IngredientBasic : AbstractDataObject {
    var name: String = ""
    var amount: Int = 0
    var unit: Int = 0
    var subDish: Boolean = false
    var disabled: Boolean = false

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        name: String,
        amount: Int,
        unit: Int,
        subDish: Boolean,
    ) {
        this.id = id.ifEmpty { StringFormatUtils.formatId() }
        this.name = name
        this.amount = amount
        this.unit = unit
        this.subDish = subDish
    }

    constructor(id: String) {
        this.id = id
    }
}
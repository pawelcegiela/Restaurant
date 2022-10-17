package pi.restaurant.management.objects.data.ingredient

import pi.restaurant.management.objects.data.AbstractDataObject
import pi.restaurant.management.utils.StringFormatUtils

class IngredientBasic : AbstractDataObject {
    lateinit var name: String
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
}
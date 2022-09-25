package pi.restaurant.management.data

import pi.restaurant.management.utils.StringFormatUtils
import java.util.*

class IngredientBasic : AbstractDataObject {
    lateinit var name: String
    var amount: Int = 0
    var unit: Int = 0
    var subDish: Boolean = false

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
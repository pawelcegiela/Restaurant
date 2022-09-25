package pi.restaurant.management.data

import pi.restaurant.management.utils.StringFormatUtils
import java.util.*

class IngredientDetails : AbstractDataObject {
    var subIngredients: MutableList<IngredientItem>? = null

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        subIngredients: MutableList<IngredientItem>?
    ) {
        this.id = id.ifEmpty { StringFormatUtils.formatId() }
        this.subIngredients = subIngredients
    }
}
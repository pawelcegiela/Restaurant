package pi.restaurant.management.data

import pi.restaurant.management.utils.StringFormatUtils
import java.util.*
import kotlin.collections.HashMap

class IngredientDetails : AbstractDataObject {
    var subIngredients: MutableList<IngredientItem>? = null
    var containingDishes: HashMap<String, Boolean> = HashMap()
    var containingSubDishes: HashMap<String, Boolean> = HashMap()

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
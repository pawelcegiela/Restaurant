package pi.restaurantapp.objects.data.ingredient

import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.AbstractDataObject

/**
 * Data class containing id, basic information and details for ingredient.
 */
class Ingredient : AbstractDataObject {
    lateinit var basic: IngredientBasic
    lateinit var details: IngredientDetails

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        basic: IngredientBasic,
        details: IngredientDetails
    ) {
        this.id = id.ifEmpty { StringFormatUtils.formatId() }
        this.basic = basic
        this.details = details
    }
}
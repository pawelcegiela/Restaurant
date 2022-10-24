package pi.restaurantapp.objects.data.ingredient

import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.utils.StringFormatUtils

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
package pi.restaurantapp.objects.data.dish

import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.AbstractDataObject

/**
 * Data class containing id, basic information and details for dish.
 */
class Dish : AbstractDataObject {
    lateinit var basic: DishBasic
    lateinit var details: DishDetails

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        basic: DishBasic,
        details: DishDetails
    ) {
        this.id = id.ifEmpty { StringFormatUtils.formatId() }
        this.basic = basic
        this.details = details
    }
}
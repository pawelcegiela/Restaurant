package pi.restaurantapp.objects.data.dish

import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.utils.StringFormatUtils

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
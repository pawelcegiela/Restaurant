package pi.restaurant.management.objects.data.dish

import pi.restaurant.management.objects.data.AbstractDataObject
import pi.restaurant.management.utils.StringFormatUtils

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
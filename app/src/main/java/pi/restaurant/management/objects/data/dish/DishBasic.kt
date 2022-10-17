package pi.restaurant.management.objects.data.dish

import pi.restaurant.management.objects.data.AbstractDataObject
import pi.restaurant.management.utils.StringFormatUtils

class DishBasic : AbstractDataObject {
    var name: String = ""
    var isActive = true
    var basePrice: String = "0.0"
    var isDiscounted: Boolean = false
    var discountPrice: String = "0.0"
    var dishType: Int = 0
    var disabled: Boolean = false

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        name: String,
        isActive: Boolean,
        basePrice: String,
        isDiscounted: Boolean,
        discountPrice: String,
        dishType: Int
    ) {
        this.id = id.ifEmpty { StringFormatUtils.formatId() }
        this.name = name
        this.isActive = isActive
        this.basePrice = basePrice
        this.isDiscounted = isDiscounted
        this.discountPrice = discountPrice
        this.dishType = dishType
    }
}
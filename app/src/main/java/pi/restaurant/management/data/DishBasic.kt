package pi.restaurant.management.data

import pi.restaurant.management.utils.StringFormatUtils

class DishBasic : AbstractDataObject {
    var name: String = ""
    var isActive = true
    var basePrice: Double = 0.0
    var isDiscounted: Boolean = false
    var discountPrice: Double = 0.0
    var dishType: Int = 0

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        name: String,
        isActive: Boolean,
        basePrice: Double,
        isDiscounted: Boolean,
        discountPrice: Double,
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
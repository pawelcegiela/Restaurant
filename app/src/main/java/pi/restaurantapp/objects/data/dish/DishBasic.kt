package pi.restaurantapp.objects.data.dish

import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.AbstractDataObject

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

    constructor(id: String) {
        this.id = id
    }

    fun setIsActive(isActive: Boolean) {
        this.isActive = isActive
    }

    fun setIsDiscounted(isDiscounted: Boolean) {
        this.isDiscounted = isDiscounted
    }
}
package pi.restaurantapp.objects.data.ingredient

import java.util.*

class IngredientAmountChange {
    var date: Date = Date()
    var userId: String = ""
    var changedValue: Int = 0
    var valueAfter: Int = 0
    var modificationType: Int = 0

    @Suppress("unused")
    constructor()

    constructor(userId: String, changedValue: Int, valueAfter: Int, modificationType: Int) {
        this.userId = userId
        this.changedValue = changedValue
        this.valueAfter = valueAfter
        this.modificationType = modificationType
    }
}
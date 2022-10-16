package pi.restaurant.management.objects.data.ingredient

import pi.restaurant.management.objects.data.AbstractDataObject

class IngredientItem : AbstractDataObject {
    var name: String = ""
    var amount: String = "0.0"
    var unit: Int = 0
    var extraPrice: String = "0.0"

    @Suppress("unused")
    constructor()

    constructor(id: String, name: String, unit: Int) {
        this.id = id
        this.name = name
        this.unit = unit
    }
}
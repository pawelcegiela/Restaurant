package pi.restaurant.management.data

class IngredientItem : AbstractDataObject {
    var name: String = ""
    var amount: Double = 0.0
    var unit: Int = 0
    var extraPrice: Double = 0.0

    @Suppress("unused")
    constructor()

    constructor(id: String, name: String, unit: Int) {
        this.id = id
        this.name = name
        this.unit = unit
    }
}
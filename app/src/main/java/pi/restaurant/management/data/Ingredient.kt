package pi.restaurant.management.data

import java.util.*

class Ingredient : AbstractDataObject {
    lateinit var name: String
    var amount: Int = 0
    var unit: Int = 0
    var subDish: Boolean = false
    var subIngredients: MutableList<IngredientItem>? = null

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        name: String,
        amount: Int,
        unit: Int,
        subDish: Boolean,
        subIngredients: MutableList<IngredientItem>?
    ) {
        if (id.isEmpty()) {
            this.id = name.replace(" ", "_") + Date().time + Random().nextInt(1000)
        } else {
            this.id = id
        }
        this.name = name
        this.amount = amount
        this.unit = unit
        this.subDish = subDish
        this.subIngredients = subIngredients
    }
}
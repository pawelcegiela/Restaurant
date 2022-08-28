package pi.restaurant.management.data

import java.util.*

class Ingredient : AbstractDataObject {
    lateinit var name: String
    var amount: Int = 0
    var unit: Int = 0

    @Suppress("unused")
    constructor()

    constructor(id: String, name: String, amount: Int, unit: Int) {
        if (id.isEmpty()) {
            this.id = name.replace(" ", "_") + Date().time + Random().nextInt(1000)
        } else {
            this.id = id
        }
        this.name = name
        this.amount = amount
        this.unit = unit
    }
}
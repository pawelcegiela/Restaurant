package pi.restaurant.management.data

import java.util.*

class Ingredient {
    lateinit var id: String
    lateinit var name: String
    var amount: Int = 0
    var unit: Int = 0

    constructor()

    constructor(name: String, amount: Int, unit: Int) {
        this.id = name + Date().time + Random().nextInt(1000)
        this.name = name
        this.amount = amount
        this.unit = unit
    }
}
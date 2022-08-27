package pi.restaurant.management.data

import java.util.*

class Dish {
    lateinit var id: String
    lateinit var name: String
    var price: Double = 0.0

    @Suppress("unused")
    constructor()

    constructor(name: String, price: Double) {
        this.id = name.replace(" ", "_") + Date().time + Random().nextInt(1000)
        this.name = name
        this.price = price
    }
}
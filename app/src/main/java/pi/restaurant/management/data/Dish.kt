package pi.restaurant.management.data

import java.util.*

class Dish : BaseDataObject {
    lateinit var name: String
    var price: Double = 0.0

    @Suppress("unused")
    constructor()

    constructor(id: String, name: String, price: Double) {
        if (id.isEmpty()) {
            this.id = name.replace(" ", "_") + Date().time + Random().nextInt(1000)
        } else {
            this.id = id
        }
        this.name = name
        this.price = price
    }
}
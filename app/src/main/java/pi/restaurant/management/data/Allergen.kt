package pi.restaurant.management.data

import java.util.*

class Allergen : AbstractDataObject {
    var name: String = ""

    @Suppress("unused")
    constructor()

    constructor(id: String, name: String) {
        if (id.isEmpty()) {
            this.id =
                name.trim().replace(" ", "_") + "_" + Date().time + "_" + Random().nextInt(1000)
        } else {
            this.id = id
        }
        this.name = name
    }
}
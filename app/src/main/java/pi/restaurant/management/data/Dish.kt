package pi.restaurant.management.data

import java.util.*
import kotlin.collections.HashMap

class Dish : AbstractDataObject {
    var photoUrl: String? = null
    var name: String = ""
    var description: String = ""
    var isActive = true
    var price: Double = 0.0
    var isDiscounted: Boolean = false
    var discountPrice: Double = 0.0
    var baseIngredients: HashMap<String, IngredientItem> = HashMap()
    var otherIngredients: HashMap<String, IngredientItem> = HashMap()
    var possibleIngredients: HashMap<String, IngredientItem> = HashMap()
    var allergens: HashMap<String, Boolean> = HashMap()
    var dishType: Int = 0
    var amount: Double = 0.0
    var unit: Int = 0

    @Suppress("unused")
    constructor()

    constructor(id: String, name: String, price: Double) {
        if (id.isEmpty()) {
            this.id =
                name.trim().replace(" ", "_") + "_" + Date().time + "_" + Random().nextInt(1000)
        } else {
            this.id = id
        }
        this.name = name
        this.price = price
    }


    // Przed zmianami
//    lateinit var name: String
//    var price: Double = 0.0
//
//    @Suppress("unused")
//    constructor()
//
//    constructor(id: String, name: String, price: Double) {
//        if (id.isEmpty()) {
//            this.id = name.replace(" ", "_") + Date().time + Random().nextInt(1000)
//        } else {
//            this.id = id
//        }
//        this.name = name
//        this.price = price
//    }
}
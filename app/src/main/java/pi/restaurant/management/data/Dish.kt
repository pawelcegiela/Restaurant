package pi.restaurant.management.data

import java.util.*
import kotlin.collections.HashMap

class Dish : AbstractDataObject {
    var photoUrl: String? = null
    var name: String = ""
    var description: String = ""
    var isActive = true
    var basePrice: Double = 0.0
    var isDiscounted: Boolean = false
    var discountPrice: Double = 0.0
    var baseIngredients: HashMap<String, IngredientItem> = HashMap()
    var otherIngredients: HashMap<String, IngredientItem> = HashMap()
    var possibleIngredients: HashMap<String, IngredientItem> = HashMap()
    var allergens: HashMap<String, Allergen> = HashMap()
    var dishType: Int = 0
    var amount: Double = 0.0
    var unit: Int = 0

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        name: String,
        description: String,
        isActive: Boolean,
        basePrice: Double,
        isDiscounted: Boolean,
        discountPrice: Double,
        baseIngredients: HashMap<String, IngredientItem>,
        otherIngredients: HashMap<String, IngredientItem>,
        possibleIngredients: HashMap<String, IngredientItem>,
        allergens: HashMap<String, Allergen>,
        dishType: Int,
        amount: Double,
        unit: Int
    ) {
        if (id.isEmpty()) {
            this.id =
                name.trim().replace(" ", "_") + "_" + Date().time + "_" + Random().nextInt(1000)
        } else {
            this.id = id
        }
        this.name = name
        this.description = description
        this.isActive = isActive
        this.basePrice = basePrice
        this.isDiscounted = isDiscounted
        this.discountPrice = discountPrice
        this.baseIngredients = baseIngredients
        this.otherIngredients = otherIngredients
        this.possibleIngredients = possibleIngredients
        this.allergens = allergens
        this.dishType = dishType
        this.amount = amount
        this.unit = unit
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
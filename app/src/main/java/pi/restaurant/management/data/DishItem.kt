package pi.restaurant.management.data

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class DishItem : AbstractDataObject, Serializable {
    lateinit var dish: Dish
    var amount: Int = 0
    var unusedOtherIngredients: ArrayList<IngredientItem> = ArrayList()
    var usedPossibleIngredients: ArrayList<IngredientItem> = ArrayList()
    var finalPrice: Double = 0.0

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        dish: Dish,
        amount: Int,
        unusedOtherIngredients: ArrayList<IngredientItem>,
        usedPossibleIngredients: ArrayList<IngredientItem>,
        finalPrice: Double
    ) {
        if (id.isEmpty()) {
            this.id =
                dish.name.trim().replace(" ", "_") + "_" + Date().time + "_" + Random().nextInt(1000)
        } else {
            this.id = id
        }
        this.dish = dish
        this.amount = amount
        this.unusedOtherIngredients = unusedOtherIngredients
        this.usedPossibleIngredients = usedPossibleIngredients
        this.finalPrice = finalPrice
    }
}
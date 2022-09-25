package pi.restaurant.management.data

import pi.restaurant.management.utils.StringFormatUtils
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
        this.id = id.ifEmpty { StringFormatUtils.formatId() }
        this.dish = dish
        this.amount = amount
        this.unusedOtherIngredients = unusedOtherIngredients
        this.usedPossibleIngredients = usedPossibleIngredients
        this.finalPrice = finalPrice
    }
}
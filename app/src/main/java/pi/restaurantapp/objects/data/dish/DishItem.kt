package pi.restaurantapp.objects.data.dish

import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.ingredient.IngredientItem
import pi.restaurantapp.utils.StringFormatUtils
import java.io.Serializable

class DishItem : AbstractDataObject, Serializable {
    lateinit var dish: Dish
    var amount: Int = 1
    var unusedOtherIngredients: ArrayList<IngredientItem> = ArrayList()
    var usedPossibleIngredients: ArrayList<IngredientItem> = ArrayList()
    var finalPrice: String = "0.0"

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        dish: Dish,
        amount: Int,
        unusedOtherIngredients: ArrayList<IngredientItem>,
        usedPossibleIngredients: ArrayList<IngredientItem>,
        finalPrice: String
    ) {
        this.id = id.ifEmpty { StringFormatUtils.formatId() }
        this.dish = dish
        this.amount = amount
        this.unusedOtherIngredients = unusedOtherIngredients
        this.usedPossibleIngredients = usedPossibleIngredients
        this.finalPrice = finalPrice
    }
}
package pi.restaurant.management.data

import pi.restaurant.management.utils.StringFormatUtils
import kotlin.collections.HashMap

class DishDetails : AbstractDataObject {
    var description: String = ""
    var recipe: String = ""
    var baseIngredients: HashMap<String, IngredientItem> = HashMap()
    var otherIngredients: HashMap<String, IngredientItem> = HashMap()
    var possibleIngredients: HashMap<String, IngredientItem> = HashMap()
    var allergens: HashMap<String, AllergenBasic> = HashMap()
    var amount: Double = 0.0
    var unit: Int = 0

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        description: String,
        recipe: String,
        baseIngredients: HashMap<String, IngredientItem>,
        otherIngredients: HashMap<String, IngredientItem>,
        possibleIngredients: HashMap<String, IngredientItem>,
        allergens: HashMap<String, AllergenBasic>,
        amount: Double,
        unit: Int
    ) {
        this.id = id.ifEmpty { StringFormatUtils.formatId() }
        this.description = description
        this.recipe = recipe
        this.baseIngredients = baseIngredients
        this.otherIngredients = otherIngredients
        this.possibleIngredients = possibleIngredients
        this.allergens = allergens
        this.amount = amount
        this.unit = unit
    }
}
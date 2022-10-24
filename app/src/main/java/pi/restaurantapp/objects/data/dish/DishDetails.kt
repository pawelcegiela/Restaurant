package pi.restaurantapp.objects.data.dish

import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.allergen.AllergenBasic
import pi.restaurantapp.objects.data.ingredient.IngredientItem
import pi.restaurantapp.utils.StringFormatUtils

class DishDetails : AbstractDataObject {
    var description: String = ""
    var recipe: String = ""
    var baseIngredients: HashMap<String, IngredientItem> = HashMap()
    var otherIngredients: HashMap<String, IngredientItem> = HashMap()
    var possibleIngredients: HashMap<String, IngredientItem> = HashMap()
    var allergens: HashMap<String, AllergenBasic> = HashMap()
    var amount: String = "0.0"
    var unit: Int = 0
    var containingOrders: HashMap<String, Boolean> = HashMap()

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
        amount: String,
        unit: Int,
        containingOrders: HashMap<String, Boolean>
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
        this.containingOrders = containingOrders
    }
}
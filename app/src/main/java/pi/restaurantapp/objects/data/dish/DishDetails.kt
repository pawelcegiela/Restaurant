package pi.restaurantapp.objects.data.dish

import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.allergen.AllergenBasic
import pi.restaurantapp.objects.data.ingredient.IngredientItem

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

    constructor(id: String) {
        this.id = id
    }
}
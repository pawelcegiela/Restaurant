package pi.restaurantapp.objects.data.ingredient

import pi.restaurantapp.objects.data.AbstractDataObject

/**
 * Data class containing details of Ingredient.
 * @see pi.restaurantapp.objects.data.ingredient.Ingredient
 */
class IngredientDetails : AbstractDataObject {
    var subIngredients: MutableList<IngredientItem>? = null
    var containingDishes: HashMap<String, Boolean> = HashMap()
    var containingSubDishes: HashMap<String, Boolean> = HashMap()
    var amountChanges: HashMap<String, IngredientAmountChange> = HashMap()
    var disableDishOnShortage: Boolean = false

    @Suppress("unused")
    constructor()

    constructor(id: String) {
        this.id = id
    }
}
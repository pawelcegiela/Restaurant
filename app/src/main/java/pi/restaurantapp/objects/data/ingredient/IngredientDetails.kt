package pi.restaurantapp.objects.data.ingredient

import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.utils.StringFormatUtils

class IngredientDetails : AbstractDataObject {
    var subIngredients: MutableList<IngredientItem>? = null
    var containingDishes: HashMap<String, Boolean> = HashMap()
    var containingSubDishes: HashMap<String, Boolean> = HashMap()
    var amountChanges: HashMap<String, IngredientAmountChange> = HashMap()

    @Suppress("unused")
    constructor()

    constructor(
        id: String,
        subIngredients: MutableList<IngredientItem>?,
        containingDishes: HashMap<String, Boolean>,
        containingSubDishes: HashMap<String, Boolean>,
        amountChanges: HashMap<String, IngredientAmountChange>
    ) {
        this.id = id.ifEmpty { StringFormatUtils.formatId() }
        this.subIngredients = subIngredients
        this.containingDishes = containingDishes
        this.containingSubDishes = containingSubDishes
        this.amountChanges = amountChanges
    }
}
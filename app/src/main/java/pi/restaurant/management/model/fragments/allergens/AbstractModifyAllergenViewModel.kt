package pi.restaurant.management.model.fragments.allergens

import pi.restaurant.management.model.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.model.fragments.dishes.EditDishViewModel
import pi.restaurant.management.objects.data.allergen.Allergen
import pi.restaurant.management.objects.data.allergen.AllergenBasic
import pi.restaurant.management.objects.data.allergen.AllergenDetails
import pi.restaurant.management.objects.data.dish.Dish

abstract class AbstractModifyAllergenViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "allergens"

    fun getPreviousItem() : Allergen {
        if (this is EditAllergenViewModel) {
            return item.value ?: Allergen(itemId, AllergenBasic(), AllergenDetails())
        }
        return Allergen()
    }
}
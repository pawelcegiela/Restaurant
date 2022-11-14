package pi.restaurantapp.model.fragments.management.allergens

import pi.restaurantapp.objects.data.allergen.Allergen
import pi.restaurantapp.objects.data.allergen.AllergenBasic
import pi.restaurantapp.objects.data.allergen.AllergenDetails
import pi.restaurantapp.utils.StringFormatUtils

class AddAllergenViewModel : AbstractModifyAllergenViewModel() {
    override fun createItem() {
        itemId = StringFormatUtils.formatId()
        setItem(Allergen(itemId, AllergenBasic(itemId), AllergenDetails(itemId)))
        setReadyToInitialize()
    }
}
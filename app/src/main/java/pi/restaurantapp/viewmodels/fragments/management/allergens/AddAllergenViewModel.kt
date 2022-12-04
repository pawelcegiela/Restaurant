package pi.restaurantapp.viewmodels.fragments.management.allergens

import pi.restaurantapp.logic.fragments.management.allergens.AddAllergenLogic
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.allergen.Allergen
import pi.restaurantapp.objects.data.allergen.AllergenBasic
import pi.restaurantapp.objects.data.allergen.AllergenDetails

class AddAllergenViewModel : AbstractModifyAllergenViewModel() {
    override val logic = AddAllergenLogic()

    override fun createItem() {
        itemId = StringFormatUtils.formatId()
        setItem(Allergen(itemId, AllergenBasic(itemId), AllergenDetails(itemId)))
        setReadyToInitialize()
    }
}
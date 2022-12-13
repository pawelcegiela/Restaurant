package pi.restaurantapp.viewmodels.fragments.management.allergens

import pi.restaurantapp.logic.fragments.management.allergens.AddAllergenLogic
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.allergen.Allergen
import pi.restaurantapp.objects.data.allergen.AllergenBasic
import pi.restaurantapp.objects.data.allergen.AllergenDetails
import pi.restaurantapp.objects.enums.ToolbarType

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for AddAllergenFragment.
 * @see pi.restaurantapp.logic.fragments.management.allergens.AddAllergenLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.allergens.AddAllergenFragment View layer
 */
class AddAllergenViewModel : AbstractModifyAllergenViewModel() {
    override val logic = AddAllergenLogic()

    override fun createItem() {
        itemId = StringFormatUtils.formatId()
        setItem(Allergen(itemId, AllergenBasic(itemId), AllergenDetails(itemId)))
        setReadyToUnlock()

        toolbarType.value = ToolbarType.SAVE
    }
}
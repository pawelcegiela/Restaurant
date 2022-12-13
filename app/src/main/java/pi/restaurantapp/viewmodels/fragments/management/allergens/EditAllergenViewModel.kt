package pi.restaurantapp.viewmodels.fragments.management.allergens

import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.management.allergens.EditAllergenLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.allergen.Allergen
import pi.restaurantapp.objects.data.allergen.AllergenBasic
import pi.restaurantapp.objects.data.allergen.AllergenDetails
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.objects.enums.ToolbarType

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for EditAllergenFragment.
 * @see pi.restaurantapp.logic.fragments.management.allergens.EditAllergenLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.allergens.EditAllergenFragment View layer
 */
class EditAllergenViewModel : AbstractModifyAllergenViewModel() {
    override val logic = EditAllergenLogic()

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<AllergenBasic>() ?: AllergenBasic()
        val details = snapshotsPair.details?.toObject<AllergenDetails>() ?: AllergenDetails()
        setItem(Allergen(itemId, basic, details))

        toolbarType.value = if (Role.isAtLeastExecutive(userRole.value)) ToolbarType.SAVE_REMOVE else ToolbarType.SAVE
    }
}
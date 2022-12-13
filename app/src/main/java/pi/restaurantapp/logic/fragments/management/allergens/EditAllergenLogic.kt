package pi.restaurantapp.logic.fragments.management.allergens

import com.google.firebase.firestore.Transaction
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.allergen.AllergenBasic
import pi.restaurantapp.objects.data.allergen.AllergenDetails

/**
 * Class responsible for business logic and communication with database (Model layer) for EditAllergenFragment.
 * @see pi.restaurantapp.ui.fragments.management.allergens.EditAllergenFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.allergens.EditAllergenViewModel ViewModel layer
 */
class EditAllergenLogic : AbstractModifyAllergenLogic() {
    override fun saveDocumentToDatabase(data: SplitDataObject, transaction: Transaction) {
        transaction.update(dbRefBasic.document(data.id), "name", (data.basic as AllergenBasic).name)
        transaction.update(dbRefDetails.document(data.id), "description", (data.details as AllergenDetails).description)
    }
}
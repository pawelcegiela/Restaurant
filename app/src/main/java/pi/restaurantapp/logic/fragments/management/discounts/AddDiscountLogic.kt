package pi.restaurantapp.logic.fragments.management.discounts

import com.google.firebase.firestore.Transaction
import pi.restaurantapp.objects.data.SplitDataObject

/**
 * Class responsible for business logic and communication with database (Model layer) for AddDiscountFragment.
 * @see pi.restaurantapp.ui.fragments.management.discounts.AddDiscountFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.discounts.AddDiscountViewModel ViewModel layer
 */
class AddDiscountLogic : AbstractModifyDiscountLogic() {
    override fun saveDocumentToDatabase(data: SplitDataObject, transaction: Transaction) {
        transaction.set(dbRefBasic.document(data.basic.id), data.basic)
        transaction.set(dbRefDetails.document(data.basic.id), data.details)
    }
}
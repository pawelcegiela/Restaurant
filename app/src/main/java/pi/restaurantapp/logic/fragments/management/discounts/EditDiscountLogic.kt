package pi.restaurantapp.logic.fragments.management.discounts

import com.google.firebase.firestore.Transaction
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.discount.DiscountBasic

/**
 * Class responsible for business logic and communication with database (Model layer) for EditDiscountFragment.
 * @see pi.restaurantapp.ui.fragments.management.discounts.EditDiscountFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.discounts.EditDiscountViewModel ViewModel layer
 */
class EditDiscountLogic : AbstractModifyDiscountLogic() {
    override fun saveDocumentToDatabase(data: SplitDataObject, transaction: Transaction) {
        transaction.update(dbRefBasic.document(data.id), "expirationDate", (data.basic as DiscountBasic).expirationDate)
    }
}
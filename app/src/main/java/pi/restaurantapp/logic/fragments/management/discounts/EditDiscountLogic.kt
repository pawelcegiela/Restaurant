package pi.restaurantapp.logic.fragments.management.discounts

import com.google.firebase.firestore.Transaction
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.discount.DiscountBasic

class EditDiscountLogic : AbstractModifyDiscountLogic() {
    override fun saveDocumentToDatabase(data: SplitDataObject, transaction: Transaction) {
        transaction.update(dbRefBasic.document(data.id), "expirationDate", (data.basic as DiscountBasic).expirationDate)
    }
}
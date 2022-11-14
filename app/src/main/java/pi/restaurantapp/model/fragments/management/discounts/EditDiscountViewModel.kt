package pi.restaurantapp.model.fragments.management.discounts

import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.discount.Discount
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.data.discount.DiscountDetails

class EditDiscountViewModel : AbstractModifyDiscountViewModel() {
    override fun saveDocumentToDatabase(data: SplitDataObject, transaction: Transaction) {
        transaction.update(dbRefBasic.document(data.id), "expirationDate", (data.basic as DiscountBasic).expirationDate)
    }

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<DiscountBasic>() ?: DiscountBasic()
        val details = snapshotsPair.details?.toObject<DiscountDetails>() ?: DiscountDetails()
        setItem(Discount(itemId, basic, details))
    }
}
package pi.restaurantapp.viewmodels.fragments.management.orders

import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.order.OrderDetails

class EditOrderViewModel : AbstractModifyOrderViewModel() {

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<OrderBasic>() ?: OrderBasic()
        val details = snapshotsPair.details?.toObject<OrderDetails>() ?: OrderDetails()
        setItem(Order(itemId, basic, details))
    }

    override fun saveDocumentToDatabase(data: SplitDataObject, transaction: Transaction) {
        val basic = data.basic as OrderBasic
        val details = data.details as OrderDetails

        val basicToChange = hashMapOf<String, Any>(
            "collectionDate" to basic.collectionDate,
            "collectionType" to basic.collectionType,
            "value" to basic.value,
            "name" to basic.name
        )
        val detailsToChange = hashMapOf(
            "orderType" to details.orderType,
            "modificationDate" to details.modificationDate,
            "orderPlace" to details.orderPlace,
            "dishes" to details.dishes,
            "address" to details.address,
            "contactPhone" to details.contactPhone,
            "comments" to details.comments
        )

        transaction.update(dbRefBasic.document(data.id), basicToChange)
        transaction.update(dbRefDetails.document(data.id), detailsToChange)
    }
}
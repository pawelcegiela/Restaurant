package pi.restaurantapp.logic.fragments.client.neworder

import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.logic.fragments.management.orders.AbstractModifyOrderLogic
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.notification.Notification
import pi.restaurantapp.objects.enums.Role
import java.util.*

/**
 * Class responsible for business logic and communication with database (Model layer) for ClientFinalizeOrderFragment.
 * Class does not contain any logic now.
 * @see pi.restaurantapp.ui.fragments.client.neworder.ClientFinalizeOrderFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.client.neworder.ClientFinalizeOrderViewModel ViewModel layer
 */
class ClientFinalizeOrderLogic : AbstractModifyOrderLogic() {
    override fun saveDocumentToDatabase(data: SplitDataObject, transaction: Transaction) {
        super.saveDocumentToDatabase(data, transaction)
        val notification = Notification(
            id = StringFormatUtils.formatId(),
            date = Date(),
            text = "Nowe zamówienie / New order",
            targetGroup = Role.WORKER.ordinal,
            targetUserId = ""
        )
        transaction.set(Firebase.firestore.collection("notifications").document(notification.id), notification)
    }
}
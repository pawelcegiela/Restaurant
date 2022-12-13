package pi.restaurantapp.logic.fragments.client.orders

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.logic.fragments.AbstractPreviewItemLogic
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.enums.OrderStatus
import java.util.*

/**
 * Class responsible for business logic and communication with database (Model layer) for ClientPreviewOrderFragment.
 * @see pi.restaurantapp.ui.fragments.client.orders.ClientPreviewOrderFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.client.orders.ClientPreviewOrderViewModel ViewModel layer
 */
class ClientPreviewOrderLogic : AbstractPreviewItemLogic() {
    override val databasePath = "orders"

    fun cancelOrder(itemId: String, callback: (Long) -> Unit) {
        val time = Date().time
        Firebase.firestore.runTransaction { transaction ->
            transaction.update(dbRefBasic.document(itemId), "orderStatus", OrderStatus.CLOSED_WITHOUT_REALIZATION.ordinal)
            transaction.update(dbRefDetails.document(itemId), "statusChanges.$time", OrderStatus.CLOSED_WITHOUT_REALIZATION.ordinal)
        }.addOnSuccessListener {
            callback(time)
        }
    }

    fun getDelivererUserName(delivererId: String, callback: (String) -> Unit) {
        Firebase.firestore.collection("users-basic").document(delivererId).get().addOnSuccessListener { snapshot ->
            val user = snapshot.toObject<UserBasic>() ?: UserBasic()
            callback(user.getFullName())
        }
    }
}
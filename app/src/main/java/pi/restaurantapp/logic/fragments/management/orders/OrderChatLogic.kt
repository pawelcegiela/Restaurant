package pi.restaurantapp.logic.fragments.management.orders

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.chat.Message
import pi.restaurantapp.objects.data.notification.Notification
import java.util.*

/**
 * Class responsible for business logic and communication with database (Model layer) for OrderChatFragment.
 * @see pi.restaurantapp.ui.fragments.management.orders.OrderChatFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.orders.OrderChatViewModel ViewModel layer
 */
class OrderChatLogic {
    private val dbRef get() = Firebase.firestore.collection("chats-orders")

    fun setFirebaseListener(orderId: String, callback: (MutableList<Message>) -> Unit) {
        dbRef.document(orderId).collection("messages").orderBy("timestamp").limitToLast(50).addSnapshotListener { snapshot, _ ->
            callback(snapshot?.mapNotNull { documentSnapshot -> documentSnapshot.toObject<Message>() }?.toMutableList() ?: ArrayList())
        }
    }

    fun addMessage(message: Message, orderId: String, messageRecipientId: String) {
        dbRef.document(orderId).collection("messages").document(message.id).set(message)

        val notification = Notification(
            id = StringFormatUtils.formatId(),
            date = Date(),
            text = "Nowa wiadomość o zamówieniu / New message about order",
            targetGroup = -1,
            targetUserId = messageRecipientId
        )
        Firebase.firestore.collection("notifications").document(notification.id).set(notification)
    }
}
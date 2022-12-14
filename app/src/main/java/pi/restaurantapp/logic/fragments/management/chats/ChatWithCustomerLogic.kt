package pi.restaurantapp.logic.fragments.management.chats

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.chat.Message
import pi.restaurantapp.objects.data.notification.Notification
import java.util.*

/**
 * Class responsible for business logic and communication with database (Model layer) for ChatWithCustomerFragment.
 * @see pi.restaurantapp.ui.fragments.management.chats.ChatWithCustomerFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.chats.ChatWithCustomerViewModel ViewModel layer
 */
class ChatWithCustomerLogic {
    private val dbRefDetails get() = Firebase.firestore.collection("chats-details")
    private val dbRefBasic get() = Firebase.firestore.collection("chats-basic")

    fun setFirebaseListener(customerId: String, callback: (MutableList<Message>) -> Unit) {
        dbRefDetails.document(customerId).collection("messages").orderBy("timestamp").limitToLast(50).addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                callback(snapshot.mapNotNull { documentSnapshot -> documentSnapshot.toObject<Message>() }.toMutableList())
            }
        }
    }

    fun addMessage(message: Message, customerId: String) {
        dbRefDetails.document(customerId).collection("messages").document(message.id).set(message)
        dbRefBasic.document(customerId).update("lastMessageDate", message.timestamp)

        val notification = Notification(
            id = StringFormatUtils.formatId(),
            date = Date(),
            text = "Nowa wiadomość / New message\nAutor / Author: ${message.authorName}",
            targetGroup = -1,
            targetUserId = customerId
        )
        Firebase.firestore.collection("notifications").document(notification.id).set(notification)
    }
}
package pi.restaurantapp.logic.fragments.management.chats

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.chat.Message
import pi.restaurantapp.objects.data.notification.Notification
import pi.restaurantapp.objects.enums.Role
import java.util.*

/**
 * Class responsible for business logic and communication with database (Model layer) for ChatWorkersForumFragment.
 * @see pi.restaurantapp.ui.fragments.management.chats.ChatWorkersForumFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.chats.ChatWorkersForumViewModel ViewModel layer
 */
class ChatWorkersForumLogic {
    private val dbRef get() = Firebase.firestore.collection("chats-forum")

    fun setFirebaseListener(callback: (MutableList<Message>) -> Unit) {
        dbRef.orderBy("timestamp").limitToLast(50).addSnapshotListener { snapshot, _ ->
            callback(snapshot?.mapNotNull { documentSnapshot -> documentSnapshot.toObject<Message>() }?.toMutableList() ?: ArrayList())
        }
    }

    fun addMessage(message: Message) {
        dbRef.document(message.id).set(message)

        val notification = Notification(
            id = StringFormatUtils.formatId(),
            date = Date(),
            text = "Nowa wiadomość na forum pracowniczym. / New message at worker forum.\nAutor / Author: ${message.authorName}",
            targetGroup = Role.WORKER.ordinal,
            targetUserId = ""
        )
        Firebase.firestore.collection("notifications").document(notification.id).set(notification)
    }
}
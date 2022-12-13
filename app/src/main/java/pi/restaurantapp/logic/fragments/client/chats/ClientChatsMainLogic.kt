package pi.restaurantapp.logic.fragments.client.chats;

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.data.chat.ChatInfo
import pi.restaurantapp.objects.data.chat.Message

/**
 * Class responsible for business logic and communication with database (Model layer) for ClientChatsMainFragment.
 * @see pi.restaurantapp.ui.fragments.client.chats.ClientChatsMainFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.client.chats.ClientChatsMainViewModel ViewModel layer
 */
class ClientChatsMainLogic {
    private val dbRefDetails = Firebase.firestore.collection("chats-details").document(Firebase.auth.uid!!)
    private val dbRefBasic = Firebase.firestore.collection("chats-basic").document(Firebase.auth.uid!!)

    fun setFirebaseListener(callback: (MutableList<Message>) -> Unit) {
        dbRefDetails.collection("messages").orderBy("timestamp").limitToLast(50).addSnapshotListener { snapshot, _ ->
            callback(snapshot?.mapNotNull { documentSnapshot -> documentSnapshot.toObject<Message>() }?.toMutableList() ?: ArrayList())
        }
    }

    fun addMessage(message: Message) {
        dbRefDetails.collection("messages").document(message.id).set(message)
        dbRefBasic.set(ChatInfo(message.authorId, message.timestamp, message.authorName))
    }
}

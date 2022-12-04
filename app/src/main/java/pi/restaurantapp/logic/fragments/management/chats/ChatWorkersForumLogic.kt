package pi.restaurantapp.logic.fragments.management.chats

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.data.chat.Message

class ChatWorkersForumLogic {
    private val dbRef get() = Firebase.firestore.collection("chats-forum")

    fun setFirebaseListener(callback: (MutableList<Message>) -> Unit) {
        dbRef.orderBy("timestamp").limitToLast(50).addSnapshotListener { snapshot, _ ->
            callback(snapshot?.mapNotNull { documentSnapshot -> documentSnapshot.toObject<Message>() }?.toMutableList() ?: ArrayList())
        }
    }

    fun addMessage(message: Message) {
        dbRef.document(message.id).set(message)
    }
}
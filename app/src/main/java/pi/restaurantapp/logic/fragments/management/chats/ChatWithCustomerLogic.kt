package pi.restaurantapp.logic.fragments.management.chats

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.data.chat.Message

class ChatWithCustomerLogic {
    private val dbRefDetails get() = Firebase.firestore.collection("chats-details")
    private val dbRefBasic get() = Firebase.firestore.collection("chats-basic")

    fun setFirebaseListener(customerId: String, callback: (MutableList<Message>) -> Unit) {
        dbRefDetails.document(customerId).collection("messages").orderBy("timestamp").limitToLast(50).addSnapshotListener { snapshot, _ ->
            callback(snapshot?.mapNotNull { documentSnapshot -> documentSnapshot.toObject<Message>() }?.toMutableList() ?: ArrayList())
        }
    }

    fun addMessage(message: Message, customerId: String) {
        dbRefDetails.document(customerId).collection("messages").document(message.id).set(message)
        dbRefBasic.document(customerId).update("lastMessageDate", message.timestamp)
    }
}
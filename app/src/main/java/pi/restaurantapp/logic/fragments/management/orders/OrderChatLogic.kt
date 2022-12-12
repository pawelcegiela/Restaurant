package pi.restaurantapp.logic.fragments.management.orders

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.data.chat.Message

class OrderChatLogic {
    private val dbRef get() = Firebase.firestore.collection("chats-orders")

    fun setFirebaseListener(orderId: String, callback: (MutableList<Message>) -> Unit) {
        dbRef.document(orderId).collection("messages").orderBy("timestamp").limitToLast(50).addSnapshotListener { snapshot, _ ->
            callback(snapshot?.mapNotNull { documentSnapshot -> documentSnapshot.toObject<Message>() }?.toMutableList() ?: ArrayList())
        }
    }

    fun addMessage(message: Message, customerId: String) {
        dbRef.document(customerId).collection("messages").document(message.id).set(message)
    }
}
package pi.restaurantapp.logic.fragments.management.orders

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.data.chat.Message

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

    fun addMessage(message: Message, customerId: String) {
        dbRef.document(customerId).collection("messages").document(message.id).set(message)
    }
}
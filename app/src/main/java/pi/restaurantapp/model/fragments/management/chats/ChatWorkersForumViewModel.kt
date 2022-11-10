package pi.restaurantapp.model.fragments.management.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.data.chat.Message

class ChatWorkersForumViewModel : ViewModel() {
    var customerId = ""

    private val dbRef get() = Firebase.firestore.collection("chats-forum")

    val messagesList: LiveData<MutableList<Message>> get() = _messagesList
    private val _messagesList = MutableLiveData<MutableList<Message>>()

    fun setFirebaseListener() {
        dbRef.orderBy("timestamp").limitToLast(50).addSnapshotListener { snapshot, _ ->
            _messagesList.value = snapshot?.mapNotNull { documentSnapshot -> documentSnapshot.toObject<Message>() }?.toMutableList() ?: ArrayList()
        }
    }

    fun addMessage(message: Message) {
        dbRef.document(message.id).set(message)
    }
}
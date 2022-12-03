package pi.restaurantapp.viewmodels.fragments.client.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.data.chat.ChatInfo
import pi.restaurantapp.objects.data.chat.Message

class ClientChatsMainViewModel : ViewModel() {
    private val dbRefDetails = Firebase.firestore.collection("chats-details").document(Firebase.auth.uid!!)
    private val dbRefBasic = Firebase.firestore.collection("chats-basic").document(Firebase.auth.uid!!)

    val messagesList: LiveData<MutableList<Message>> get() = _messagesList
    private val _messagesList = MutableLiveData<MutableList<Message>>()

    fun setFirebaseListener() {
        dbRefDetails.collection("messages").orderBy("timestamp").limitToLast(50).addSnapshotListener { snapshot, _ ->
            _messagesList.value = snapshot?.mapNotNull { documentSnapshot -> documentSnapshot.toObject<Message>() }?.toMutableList() ?: ArrayList()
        }
    }

    fun addMessage(message: Message) {
        dbRefDetails.collection("messages").document(message.id).set(message)
        dbRefBasic.set(ChatInfo(message.authorId, message.timestamp, message.authorName))
    }
}
package pi.restaurantapp.model.fragments.client.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.data.chat.ChatInfo
import pi.restaurantapp.objects.data.chat.Message

class ClientChatsMainViewModel : ViewModel() {
    private val databaseRefMessages = Firebase.database.getReference("chats").child("details").child(Firebase.auth.uid ?: "ERROR")
    private val databaseRefInfo = Firebase.database.getReference("chats").child("basic").child(Firebase.auth.uid ?: "ERROR")

    val messagesList: LiveData<ArrayList<Message>> get() = _messagesList
    private val _messagesList = MutableLiveData<ArrayList<Message>>()

    fun setFirebaseListener() {
        databaseRefMessages.child("messages").orderByChild("timestamp").limitToLast(40).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val toReturn: ArrayList<Message> = ArrayList()

                for (data in dataSnapshot.children) {
                    val messageData = data.getValue<Message>()
                    val message = messageData ?: continue
                    toReturn.add(message)
                }

                _messagesList.value = toReturn
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun addMessage(message: Message) {
        databaseRefMessages.child("messages").child(message.id).setValue(message)
        databaseRefInfo.setValue(ChatInfo(message.authorId, message.timestamp, message.authorName))
    }
}
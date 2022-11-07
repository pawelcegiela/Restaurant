package pi.restaurantapp.model.fragments.management.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.data.chat.Message

class ChatWithCustomerViewModel : ViewModel() {
    var customerId = ""

    private val databaseRefMessages get() = Firebase.database.getReference("chats").child("details").child(customerId)
    private val databaseRefInfo get() = Firebase.database.getReference("chats").child("basic").child(customerId)

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
        databaseRefInfo.child("lastMessageDate").setValue(message.timestamp)
    }
}
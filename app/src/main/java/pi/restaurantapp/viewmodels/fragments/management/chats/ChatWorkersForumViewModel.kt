package pi.restaurantapp.viewmodels.fragments.management.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pi.restaurantapp.logic.fragments.management.chats.ChatWorkersForumLogic
import pi.restaurantapp.objects.data.chat.Message

class ChatWorkersForumViewModel : ViewModel() {
    private val logic = ChatWorkersForumLogic()
    var customerId = ""

    val messagesList: LiveData<MutableList<Message>> get() = _messagesList
    private val _messagesList = MutableLiveData<MutableList<Message>>()

    fun setFirebaseListener() {
        logic.setFirebaseListener { messages ->
            _messagesList.value = messages
        }
    }

    fun addMessage(message: Message) {
        logic.addMessage(message)
    }
}
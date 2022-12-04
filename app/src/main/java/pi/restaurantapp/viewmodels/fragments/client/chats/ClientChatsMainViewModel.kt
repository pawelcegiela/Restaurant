package pi.restaurantapp.viewmodels.fragments.client.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pi.restaurantapp.logic.fragments.client.chats.ClientChatsMainLogic
import pi.restaurantapp.objects.data.chat.Message

class ClientChatsMainViewModel : ViewModel() {
    private val logic: ClientChatsMainLogic = ClientChatsMainLogic()

    val messagesList: LiveData<MutableList<Message>> get() = _messagesList
    private val _messagesList = MutableLiveData<MutableList<Message>>()

    fun setFirebaseListener() {
        logic.setFirebaseListener { messagesList ->
            _messagesList.value = messagesList
        }
    }

    fun addMessage(message: Message) {
        logic.addMessage(message)
    }
}
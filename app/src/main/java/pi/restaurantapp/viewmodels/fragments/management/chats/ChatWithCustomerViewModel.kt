package pi.restaurantapp.viewmodels.fragments.management.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pi.restaurantapp.logic.fragments.management.chats.ChatWithCustomerLogic
import pi.restaurantapp.objects.data.chat.Message

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for ChatWithCustomerFragment.
 * @see pi.restaurantapp.logic.fragments.management.chats.ChatWithCustomerLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.chats.ChatWithCustomerFragment View layer
 */
class ChatWithCustomerViewModel : ViewModel() {
    private val logic = ChatWithCustomerLogic()
    var customerId = ""

    val messagesList: LiveData<MutableList<Message>> get() = _messagesList
    private val _messagesList = MutableLiveData<MutableList<Message>>()

    fun setFirebaseListener() {
        logic.setFirebaseListener(customerId) { messages ->
            _messagesList.value = messages
        }
    }

    fun addMessage(message: Message) {
        logic.addMessage(message, customerId)
    }
}
package pi.restaurantapp.viewmodels.fragments.management.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pi.restaurantapp.logic.fragments.management.orders.OrderChatLogic
import pi.restaurantapp.objects.data.chat.Message

class OrderChatViewModel : ViewModel() {
    private val logic = OrderChatLogic()
    var orderId = ""

    val messagesList: LiveData<MutableList<Message>> get() = _messagesList
    private val _messagesList = MutableLiveData<MutableList<Message>>()

    fun setFirebaseListener() {
        logic.setFirebaseListener(orderId) { messages ->
            _messagesList.value = messages
        }
    }

    fun addMessage(message: Message) {
        logic.addMessage(message, orderId)
    }
}
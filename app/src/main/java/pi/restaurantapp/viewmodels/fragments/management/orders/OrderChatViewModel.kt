package pi.restaurantapp.viewmodels.fragments.management.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pi.restaurantapp.logic.fragments.management.orders.OrderChatLogic
import pi.restaurantapp.objects.data.chat.Message

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for OrderChatFragment.
 * @see pi.restaurantapp.logic.fragments.management.orders.OrderChatLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.orders.OrderChatFragment View layer
 */
class OrderChatViewModel : ViewModel() {
    private val logic = OrderChatLogic()
    var orderId = ""
    var messageRecipientId = ""

    val messagesList: LiveData<MutableList<Message>> get() = _messagesList
    private val _messagesList = MutableLiveData<MutableList<Message>>()

    fun setFirebaseListener() {
        logic.setFirebaseListener(orderId) { messages ->
            _messagesList.value = messages
        }
    }

    fun addMessage(message: Message) {
        logic.addMessage(message, orderId, messageRecipientId)
    }
}
package pi.restaurantapp.viewmodels.fragments.management.chats

import pi.restaurantapp.logic.fragments.management.chats.ChatsChooseCustomerLogic
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel

class ChatsChooseCustomerViewModel : AbstractItemListViewModel() {
    override val logic = ChatsChooseCustomerLogic()

    override fun displayFAB() = false
}
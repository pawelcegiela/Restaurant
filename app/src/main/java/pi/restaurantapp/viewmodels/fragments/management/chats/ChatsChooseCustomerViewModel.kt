package pi.restaurantapp.viewmodels.fragments.management.chats

import pi.restaurantapp.logic.fragments.management.chats.ChatsChooseCustomerLogic
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for ChatsChooseCustomerFragment.
 * @see pi.restaurantapp.logic.fragments.management.chats.ChatsChooseCustomerLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.chats.ChatsChooseCustomerFragment View layer
 */
class ChatsChooseCustomerViewModel : AbstractItemListViewModel() {
    override val logic = ChatsChooseCustomerLogic()

    override fun displayFAB() = false

    override fun loadData() {
        logic.loadData { list ->
            if (Role.isAtLeastManager(userRole.value)) {
                setDataList(list)
            } else {
                setDataList(ArrayList())
            }
        }
    }
}
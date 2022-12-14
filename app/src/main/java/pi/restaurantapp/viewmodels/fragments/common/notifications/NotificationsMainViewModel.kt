package pi.restaurantapp.viewmodels.fragments.common.notifications

import pi.restaurantapp.logic.fragments.common.notifications.NotificationsMainLogic
import pi.restaurantapp.objects.enums.Role
import pi.restaurantapp.viewmodels.fragments.AbstractItemListViewModel

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for NotificationsMainFragment.
 * @see pi.restaurantapp.logic.fragments.common.notifications.NotificationsMainLogic Model layer
 * @see pi.restaurantapp.ui.fragments.common.notifications.NotificationsMainFragment View layer
 */
class NotificationsMainViewModel : AbstractItemListViewModel() {
    override val logic = NotificationsMainLogic()

    override fun loadData() {
        logic.loadData(userRole.value ?: Role.getPlaceholder()) { list ->
            setDataList(list)
        }
    }
}
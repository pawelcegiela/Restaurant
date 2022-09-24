package pi.restaurant.management.fragments.orders

import pi.restaurant.management.R

class AddOrderFragment : AbstractModifyOrderFragment() {

    override val nextActionId = R.id.actionAddOrderToOrders
    override val saveMessageId = R.string.order_added
    override val removeMessageId = 0 // Unused
    override val addDishAction = R.id.actionAddOrderToDishes

    override fun initializeUI() {
        super.initializeUI()
        initializeRecycler()
        addLiveDataListener()
        finishLoading()
    }
}
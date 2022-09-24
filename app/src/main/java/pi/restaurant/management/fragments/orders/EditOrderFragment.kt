package pi.restaurant.management.fragments.orders

import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.R
import pi.restaurant.management.data.Order
import pi.restaurant.management.utils.StringFormatUtils


class EditOrderFragment : AbstractModifyOrderFragment() {

    override val nextActionId = R.id.actionEditOrderToOrders
    override val saveMessageId = R.string.order_modified
    override val removeMessageId = R.string.order_removed
    override val addDishAction = R.id.actionEditOrderToDishes

    override fun initializeUI() {
        super.initializeUI()

        itemId = arguments?.getString("id").toString()
        removeButton.visibility = View.VISIBLE

        setRemoveButtonListener()
        getDataFromDatabase()
    }

    override fun fillInData(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue<Order>() ?: return
        binding.spinnerType.setSelection(data.orderType)
        binding.spinnerStatus.setSelection(data.orderStatus)
        binding.editTextCollectionTime.setText(StringFormatUtils.formatTime(data.collectionDate))
        binding.spinnerDelivery.setSelection(data.deliveryType)
        binding.spinnerPlace.setSelection(data.orderPlace)
        dishesList = data.dishes.toList().map { it.second }.toMutableList()

        addLiveDataListener()
        initializeRecycler()

        finishLoading()
    }
}
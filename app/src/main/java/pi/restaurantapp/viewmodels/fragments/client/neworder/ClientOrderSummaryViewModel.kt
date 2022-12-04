package pi.restaurantapp.viewmodels.fragments.client.neworder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.client.neworder.ClientOrderSummaryLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.data.dish.DishItem
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.order.OrderDetails
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel

class ClientOrderSummaryViewModel : AbstractPreviewItemViewModel() {
    override val logic = ClientOrderSummaryLogic()

    private val _possibleDiscounts = MutableLiveData<MutableList<DiscountBasic>>()
    val possibleDiscounts: LiveData<MutableList<DiscountBasic>> get() = _possibleDiscounts

    private val _item: MutableLiveData<Order> = MutableLiveData()
    val item: LiveData<Order> = _item

    val dishesList = ArrayList<DishItem>()

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<OrderBasic>() ?: OrderBasic()
        val details = snapshotsPair.details?.toObject<OrderDetails>() ?: OrderDetails()
        _item.value = Order(itemId, basic, details)
    }

    fun initializeData() {
        dishesList.addAll(_item.value!!.details.dishes.toList().map { it.second })
        getPossibleDiscounts()
        setReadyToUnlock()
    }

    private fun getPossibleDiscounts() {
        logic.getPossibleDiscounts { possibleDiscounts ->
            _possibleDiscounts.value = possibleDiscounts
        }
    }

    fun redeemDiscount(oldPrice: String, discountToRedeem: DiscountBasic, callback: (Boolean) -> Unit) {
        logic.redeemDiscount(itemId, oldPrice, discountToRedeem, callback)
    }

    fun setItem(order: Order) {
        _item.value = order
    }

    override fun shouldGetDataFromDatabase() = false

    override fun isDisabled(): Boolean {
        return item.value?.basic?.disabled == true
    }
}
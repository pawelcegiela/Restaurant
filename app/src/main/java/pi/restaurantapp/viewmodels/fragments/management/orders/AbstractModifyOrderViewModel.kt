package pi.restaurantapp.viewmodels.fragments.management.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.logic.fragments.management.orders.AbstractModifyOrderLogic
import pi.restaurantapp.logic.utils.ComputingUtils
import pi.restaurantapp.logic.utils.PreconditionUtils
import pi.restaurantapp.objects.data.NullableSplitDataObject
import pi.restaurantapp.objects.data.delivery.DeliveryBasic
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.enums.CollectionType
import pi.restaurantapp.objects.enums.Precondition
import pi.restaurantapp.viewmodels.activities.management.OrdersViewModel
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel

abstract class AbstractModifyOrderViewModel : AbstractModifyItemViewModel() {
    private val _logic: AbstractModifyOrderLogic get() = logic as AbstractModifyOrderLogic

    private val _item: MutableLiveData<Order> = MutableLiveData()
    val item: LiveData<Order> = _item

    override val splitDataObject
        get() = run {
            if (activityViewModel.previousStatus.value == null) {
                activityViewModel.setPreviousStatus(item.value?.basic?.orderStatus)
            }
            if (activityViewModel.deliveryOptions.value == null) {
                activityViewModel.setDeliveryOptions(observer.deliveryOptions)
            }

            return@run NullableSplitDataObject(item.value?.id ?: itemId, item.value?.basic, item.value?.details)
        }

    private val _previousStatus: MutableLiveData<Int> = MutableLiveData()
    val previousStatus: LiveData<Int> = _previousStatus

    var observer = ModifyOrderObserver(_item) { updateFullPrice() }

    lateinit var activityViewModel: OrdersViewModel

    abstract fun setToolbarType()

    fun updateFullPrice() {
        observer.value = ComputingUtils.countFullOrderPrice(observer.dishesList, item.value!!.basic.collectionType, observer.deliveryOptions)
    }

    fun setItem(order: Order) {
        _item.value = order
        observer.dishesList = order.details.dishes.toList().map { it.second }.toMutableList()
        if (previousStatus.value == null) {
            setPreviousStatus(activityViewModel.previousStatus.value ?: -1)
        }
    }

    fun setPreviousStatus(status: Int) {
        _previousStatus.value = status
    }

    fun setDeliveryOptions(deliveryOptions: DeliveryBasic?) {
        if (deliveryOptions != null) {
            observer.deliveryOptions = deliveryOptions
        } else {
            _logic.getAdditionalData { newDeliveryOptions ->
                observer.deliveryOptions = newDeliveryOptions
            }
        }
    }

    override fun shouldGetDataFromDatabase(): Boolean {
        if (item.value != null) {
            setReadyToUnlock()
        }
        return item.value == null
    }

    fun isDeliveryOrder(): Boolean {
        return item.value?.basic?.collectionType == CollectionType.DELIVERY.ordinal
    }

    override fun checkSavePreconditions(): Precondition {
        if (super.checkSavePreconditions() != Precondition.OK) {
            return super.checkSavePreconditions()
        }
        return PreconditionUtils.checkOrder(item.value!!.basic, item.value!!.details, observer.deliveryOptions)
    }
}
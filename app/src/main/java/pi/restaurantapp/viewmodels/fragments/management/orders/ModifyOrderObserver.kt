package pi.restaurantapp.viewmodels.fragments.management.orders

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.delivery.DeliveryBasic
import pi.restaurantapp.objects.data.dish.DishItem
import pi.restaurantapp.objects.data.order.Order

/**
 * Class responsible for observing and updating fields (ViewModel layer) for AbstractModifyOrderViewModel.
 * @see pi.restaurantapp.viewmodels.fragments.management.orders.AbstractModifyOrderViewModel ViewModel
 */
class ModifyOrderObserver(private val item: MutableLiveData<Order>, private val updateFullPrice: () -> (Unit)) : BaseObservable() {
    @get:Bindable
    var dishesList: MutableList<DishItem> = ArrayList()
        set(value) {
            field = value
            notifyPropertyChanged(BR.dishesList)
            updateFullPrice()
        }

    @get:Bindable
    var value: String = "0.0"
        set(value) {
            field = StringFormatUtils.formatPrice(value)
            notifyPropertyChanged(BR.value)
            item.value!!.basic.value = value
        }

    @get:Bindable
    var deliveryOptions: DeliveryBasic = DeliveryBasic()
        set(value) {
            field = value
            notifyPropertyChanged(BR.deliveryOptions)
        }
}
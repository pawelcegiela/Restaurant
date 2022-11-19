package pi.restaurantapp.model.fragments.management.orders

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.model.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.objects.data.delivery.DeliveryBasic
import pi.restaurantapp.objects.data.dish.DishItem
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.enums.CollectionType
import pi.restaurantapp.utils.ComputingUtils
import pi.restaurantapp.utils.StringFormatUtils

abstract class AbstractModifyOrderViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "orders"

    private val _item: MutableLiveData<Order> = MutableLiveData()
    val item: LiveData<Order> = _item

    private val _previousStatus: MutableLiveData<Int> = MutableLiveData()
    val previousStatus: LiveData<Int> = _previousStatus

    var observer = Observer(_item) { updateFullPrice() }

    class Observer(private val item: MutableLiveData<Order>, private val updateFullPrice: () -> (Unit)) : BaseObservable() {
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

    fun updateFullPrice() {
        observer.value = ComputingUtils.countFullOrderPrice(observer.dishesList, item.value!!.basic.collectionType, observer.deliveryOptions)
    }

    fun setItem(order: Order) {
        _item.value = order
        observer.dishesList = order.details.dishes.toList().map { it.second }.toMutableList()
    }

    fun setPreviousStatus(status: Int) {
        _previousStatus.value = status
    }

    fun setDeliveryOptions(deliveryOptions: DeliveryBasic?) {
        if (deliveryOptions != null) {
            observer.deliveryOptions = deliveryOptions
        } else {
            getAdditionalData()
        }
    }

    private fun getAdditionalData() {
        Firebase.firestore.collection("restaurantData-basic").document("delivery").get().addOnSuccessListener { snapshot ->
            observer.deliveryOptions = snapshot.toObject() ?: DeliveryBasic()
        }
    }

    override fun shouldGetDataFromDatabase(): Boolean {
        if (item.value != null) {
            setReadyToInitialize()
        }
        return item.value == null
    }

    fun isDeliveryOrder(): Boolean {
        return item.value?.basic?.collectionType == CollectionType.DELIVERY.ordinal
    }
}
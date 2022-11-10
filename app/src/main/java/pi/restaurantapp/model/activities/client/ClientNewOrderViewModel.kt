package pi.restaurantapp.model.activities.client

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.R
import pi.restaurantapp.model.activities.management.OrdersViewModel
import pi.restaurantapp.objects.data.address.AddressBasic
import pi.restaurantapp.objects.data.delivery.DeliveryBasic
import pi.restaurantapp.objects.data.dish.DishItem
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.order.OrderDetails

class ClientNewOrderViewModel : OrdersViewModel() {
    init {
        setActionSave(R.id.actionClientCustomizeDishToNewOrder)
    }

    fun createSavedOrder(address: AddressBasic, contactPhone: String, collectionType: Int, orderPlace: Int) {
        val order = Order("", OrderBasic(), OrderDetails())
        order.basic.collectionType = collectionType
        order.details.address = address
        order.details.contactPhone = contactPhone
        order.details.orderPlace = orderPlace
        setSavedOrder(order)
    }

    fun addDishItem(dishItem: DishItem) {
        savedOrder.value!!.details.dishes[dishItem.id] = dishItem
    }

    fun setDeliveryOptions() {
        Firebase.firestore.collection("restaurantData-basic").document("delivery").get().addOnSuccessListener { snapshot ->
            setDeliveryOptions(snapshot.toObject() ?: DeliveryBasic())
        }
    }
}
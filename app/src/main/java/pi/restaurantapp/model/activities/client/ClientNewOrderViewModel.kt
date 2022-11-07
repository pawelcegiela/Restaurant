package pi.restaurantapp.model.activities.client

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
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
        val databaseRef = Firebase.database.getReference("restaurantData").child("basic").child("delivery")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                setDeliveryOptions(dataSnapshot.getValue<DeliveryBasic>() ?: DeliveryBasic())
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
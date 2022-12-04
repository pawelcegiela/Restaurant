package pi.restaurantapp.logic.fragments.management.orders

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.logic.fragments.AbstractModifyItemLogic
import pi.restaurantapp.objects.data.delivery.DeliveryBasic

abstract class AbstractModifyOrderLogic : AbstractModifyItemLogic() {
    override val databasePath = "orders"

    fun getAdditionalData(callback: (DeliveryBasic) -> Unit) {
        Firebase.firestore.collection("restaurantData-basic").document("delivery").get().addOnSuccessListener { snapshot ->
            callback(snapshot.toObject() ?: DeliveryBasic())
        }
    }
}
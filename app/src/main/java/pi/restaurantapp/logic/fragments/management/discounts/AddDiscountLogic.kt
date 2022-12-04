package pi.restaurantapp.logic.fragments.management.discounts

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.logic.utils.ComputingUtils

class AddDiscountLogic : AbstractModifyDiscountLogic() {
    fun algorithm() {
        // TODO Do odpowiedniego miejsca
        Firebase.firestore.collection("orders-basic")
            .whereGreaterThan("collectionDate", ComputingUtils.getMonthAgoDate())
            .orderBy("collectionDate", Query.Direction.DESCENDING).get().addOnSuccessListener { snapshot ->
                val rewards = ComputingUtils.countDiscountRewards(snapshot.map { document -> document.toObject() }, 100L, 2L)
                // TODO Dodawanie do bazy
            }
    }
}
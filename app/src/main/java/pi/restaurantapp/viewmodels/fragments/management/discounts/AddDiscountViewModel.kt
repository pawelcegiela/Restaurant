package pi.restaurantapp.viewmodels.fragments.management.discounts

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.data.discount.Discount
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.data.discount.DiscountDetails
import pi.restaurantapp.logic.utils.ComputingUtils

class AddDiscountViewModel : AbstractModifyDiscountViewModel() {
    override fun createItem() {
        setItem(Discount("", DiscountBasic(), DiscountDetails()))
        setReadyToInitialize()
    }

    fun algorithm() {
        Firebase.firestore.collection("orders-basic")
            .whereGreaterThan("collectionDate", ComputingUtils.getMonthAgoDate())
            .orderBy("collectionDate", Query.Direction.DESCENDING).get().addOnSuccessListener { snapshot ->
                val rewards = ComputingUtils.countDiscountRewards(snapshot.map { document -> document.toObject() }, 100L, 2L)
                // TODO Dodawanie do bazy
            }
    }

}
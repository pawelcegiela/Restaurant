package pi.restaurantapp.model.fragments.client.neworder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.model.fragments.client.orders.ClientPreviewOrderViewModel
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.utils.ComputingUtils
import java.util.*

class ClientOrderSummaryViewModel : ClientPreviewOrderViewModel() {
    private val _possibleDiscounts = MutableLiveData<MutableList<DiscountBasic>>()
    val possibleDiscounts: LiveData<MutableList<DiscountBasic>> get() = _possibleDiscounts

    override fun shouldGetDataFromDatabase() = false

    fun getPossibleDiscounts() {
        Firebase.firestore.collection("discounts-basic")
            .whereArrayContains("assignedDiscounts", Firebase.auth.uid!!)
            .whereGreaterThan("expirationDate", Date()).get().addOnSuccessListener { snapshot ->
                _possibleDiscounts.value = snapshot.map { document -> document.toObject<DiscountBasic>() }.toMutableList()
            }
    }

    fun redeemDiscount(oldPrice: String, discountToRedeem: DiscountBasic, callback: (Boolean) -> Unit) {
        var success = true
        Firebase.firestore.runTransaction { transaction ->
            val discount = transaction.get(Firebase.firestore.collection("discounts-basic").document(discountToRedeem.id)).toObject<DiscountBasic>()
                ?: return@runTransaction
            if (!discount.assignedDiscounts.contains(Firebase.auth.uid)) {
                success = false
                return@runTransaction
            }
            val idToSave = if (discount.redeemedDiscounts.contains(Firebase.auth.uid)) "${Firebase.auth.uid}#${Date().time}" else Firebase.auth.uid
            transaction.update(Firebase.firestore.collection("orders-details").document(itemId), "discount", discount.id)
            transaction.update(
                Firebase.firestore.collection("orders-basic").document(itemId),
                "value",
                ComputingUtils.countPriceAfterDiscount(oldPrice, discount)
            )
            transaction.update(
                Firebase.firestore.collection("discounts-basic").document(discount.id),
                "assignedDiscounts",
                FieldValue.arrayRemove(Firebase.auth.uid)
            )
            transaction.update(
                Firebase.firestore.collection("discounts-basic").document(discount.id),
                "redeemedDiscounts",
                FieldValue.arrayUnion(idToSave)
            )
        }.addOnSuccessListener {
            callback(success)
        }
    }
}
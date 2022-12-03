package pi.restaurantapp.viewmodels.fragments.client.neworder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.data.dish.DishItem
import pi.restaurantapp.objects.data.order.Order
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.data.order.OrderDetails
import pi.restaurantapp.logic.utils.ComputingUtils
import java.util.*

class ClientOrderSummaryViewModel : AbstractPreviewItemViewModel() {
    override val databasePath = "orders"

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

    fun setItem(order: Order) {
        _item.value = order
    }

    override fun shouldGetDataFromDatabase() = false

    override fun isDisabled(): Boolean {
        return item.value?.basic?.disabled == true
    }
}
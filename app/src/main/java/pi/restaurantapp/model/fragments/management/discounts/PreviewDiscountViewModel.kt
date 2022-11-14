package pi.restaurantapp.model.fragments.management.discounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.R
import pi.restaurantapp.model.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.discount.Discount
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.data.discount.DiscountDetails
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.enums.DiscountUsageType
import pi.restaurantapp.objects.enums.Role
import java.util.*

class PreviewDiscountViewModel : AbstractPreviewItemViewModel() {
    override val databasePath = "discounts"

    private val _item: MutableLiveData<Discount> = MutableLiveData()
    val item: LiveData<Discount> = _item

    private val _customers: MutableLiveData<MutableList<UserBasic>> = MutableLiveData()
    val customers: LiveData<MutableList<UserBasic>> = _customers

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<DiscountBasic>() ?: DiscountBasic()
        val details = DiscountDetails()
        _item.value = Discount(itemId, basic, details)
    }

    override fun isDisabled(): Boolean {
        return item.value?.basic?.disabled == true
    }

    fun getCustomers() {
        Firebase.firestore.collection("users-basic").whereEqualTo("role", Role.CUSTOMER.ordinal).get().addOnSuccessListener { snapshot ->
            val dataList = snapshot.map { document -> document.toObject<UserBasic>() }.toMutableList()
            _customers.value = dataList
        }
    }

    fun addCustomer(customer: UserBasic, callback: (Int, Boolean) -> (Unit)) {
        var newDiscountMessageId = 0
        var success = false
        Firebase.firestore.runTransaction { transaction ->
            val discount = transaction.get(dbRefBasic.document(itemId)).toObject<DiscountBasic>()
            if (discount == null || discount.expirationDate < Date() || discount.disabled) {
                newDiscountMessageId = R.string.discount_does_not_exist
            } else if (discount.numberOfDiscounts <= discount.assignedDiscounts.size + discount.redeemedDiscounts.size) {
                newDiscountMessageId = R.string.there_are_no_available_discounts_with_this_code_anymore
            } else if (discount.assignedDiscounts.contains(customer.id)
                || (discount.usageType == DiscountUsageType.ONE_TIME.ordinal && discount.redeemedDiscounts.contains(customer.id))
            ) {
                newDiscountMessageId = R.string.user_has_this_account
            } else {
                newDiscountMessageId = R.string.discount_has_been_assigned_to_this_customer
                transaction.update(
                    Firebase.firestore.collection("discounts-basic").document(itemId),
                    "assignedDiscounts", FieldValue.arrayUnion(customer.id)
                )
                success = true
            }
        }.addOnSuccessListener {
            if (success) {
                item.value!!.basic.assignedDiscounts.add(customer.id)
            }
            callback(newDiscountMessageId, success)
        }
    }
}
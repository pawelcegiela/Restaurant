package pi.restaurantapp.logic.fragments.client.discounts

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.R
import pi.restaurantapp.logic.fragments.AbstractItemListLogic
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.enums.DiscountReceiverType
import pi.restaurantapp.objects.enums.DiscountUsageType
import java.util.*

class ClientDiscountsMainLogic : AbstractItemListLogic() {
    override val databasePath = "discounts"
    override val dbRef get() = super.dbRef.whereArrayContains("assignedDiscounts", Firebase.auth.uid!!).whereGreaterThan("expirationDate", Date())

    override fun retrieveDataList(snapshot: QuerySnapshot, callback: (MutableList<AbstractDataObject>) -> Unit) {
        val dataList = snapshot.map { document -> document.toObject<DiscountBasic>() }.toMutableList<AbstractDataObject>()
        callback(dataList)
    }

    fun addNewDiscount(code: String, callbackMessage: (Int) -> Unit, callbackDiscount: (DiscountBasic) -> Unit) {
        var newDiscountMessageId = 0
        var newDiscountVal: DiscountBasic? = null
        Firebase.firestore.runTransaction { transaction ->
            val discount = transaction.get(Firebase.firestore.collection("discounts-basic").document(code)).toObject<DiscountBasic>()
            if (discount == null || discount.expirationDate < Date() || discount.disabled) {
                newDiscountMessageId = R.string.discount_does_not_exist
            } else if (discount.receiverType == DiscountReceiverType.SPECIFIC_PEOPLE.ordinal) {
                newDiscountMessageId = R.string.discount_can_be_assigned_by_restaurant
            } else if (discount.numberOfDiscounts <= discount.assignedDiscounts.size + discount.redeemedDiscounts.size) {
                newDiscountMessageId = R.string.there_are_no_available_discounts_with_this_code_anymore
            } else if (discount.assignedDiscounts.contains(Firebase.auth.uid)
                || (discount.usageType == DiscountUsageType.ONE_TIME.ordinal && discount.redeemedDiscounts.contains(Firebase.auth.uid))
            ) {
                newDiscountMessageId = R.string.you_already_assigned_discount
            } else {
                newDiscountMessageId = R.string.discount_has_been_assigned_to_your_account
                newDiscountVal = discount
                transaction.update(
                    Firebase.firestore.collection("discounts-basic").document(code),
                    "assignedDiscounts", FieldValue.arrayUnion(Firebase.auth.uid)
                )
            }
        }.addOnSuccessListener {
            callbackMessage(newDiscountMessageId)
            if (newDiscountVal != null) {
                callbackDiscount(newDiscountVal!!)
            }
        }
    }
}
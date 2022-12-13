package pi.restaurantapp.logic.fragments.management.discounts

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.logic.fragments.AbstractFragmentLogic
import pi.restaurantapp.logic.utils.ComputingUtils
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.data.order.OrderBasic
import pi.restaurantapp.objects.enums.OrderType

/**
 * Class responsible for business logic and communication with database (Model layer) for AddRewardFragment.
 * @see pi.restaurantapp.ui.fragments.management.discounts.AddRewardFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.discounts.AddRewardViewModel ViewModel layer
 */
class AddRewardLogic : AbstractFragmentLogic() {
    override val databasePath = ""

    fun generateDiscountRewards(totalValue: Long, minimumValue: Long, days: Long, callback: (Boolean, ArrayList<DiscountBasic>) -> Unit) {
        Firebase.firestore.collection("orders-basic")
            .whereGreaterThan("collectionDate", ComputingUtils.getDateXDaysAgo(days))
            .orderBy("collectionDate", Query.Direction.DESCENDING).get().addOnSuccessListener { snapshot ->
                val rewards = ComputingUtils.countDiscountRewards(snapshot.map { document -> document.toObject<OrderBasic>() }
                    .filter { it.orderType == OrderType.CLIENT_APP.ordinal }, totalValue, minimumValue)
                addRewardsToDatabase(rewards, callback)
            }
    }

    private fun addRewardsToDatabase(rewardValues: HashMap<String, Int>, callback: (Boolean, ArrayList<DiscountBasic>) -> Unit) {
        val discountsToAdd = ArrayList<DiscountBasic>()
        for (key in rewardValues.keys) {
            val discount = DiscountBasic(
                id = StringFormatUtils.formatRewardId(key),
                amount = rewardValues[key].toString(),
                userId = key
            )
            discountsToAdd.add(discount)
        }

        Firebase.firestore.runTransaction { transaction ->
            for (discount in discountsToAdd) {
                transaction.set(Firebase.firestore.collection("discounts-basic").document(discount.id), discount)
            }
        }.addOnSuccessListener {
            callback(true, discountsToAdd)
        }.addOnFailureListener {
            callback(false, ArrayList())
        }
    }
}
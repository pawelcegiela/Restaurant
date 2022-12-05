package pi.restaurantapp.logic.fragments.management.discounts

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.logic.fragments.AbstractFragmentLogic
import pi.restaurantapp.logic.utils.ComputingUtils
import java.util.HashMap

class AddRewardLogic : AbstractFragmentLogic() {
    override val databasePath = ""

    fun generateDiscountRewards(totalValue: Long, minimumValue: Long, days: Long) {
        Firebase.firestore.collection("orders-basic")
            .whereGreaterThan("collectionDate", ComputingUtils.getDateXDaysAgo(days))
            .orderBy("collectionDate", Query.Direction.DESCENDING).get().addOnSuccessListener { snapshot ->
                val rewards = ComputingUtils.countDiscountRewards(snapshot.map { document -> document.toObject() }, totalValue, minimumValue)
                addRewardsToDatabase(rewards)
            }
    }

    private fun addRewardsToDatabase(rewards: HashMap<String, Int>) {
        // TODO
    }
}
package pi.restaurantapp.logic.fragments.common.notifications

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.logic.fragments.AbstractItemListLogic
import pi.restaurantapp.logic.utils.ComputingUtils
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.notification.Notification

/**
 * Class responsible for business logic and communication with database (Model layer) for NotificationsMainFragment.
 * @see pi.restaurantapp.ui.fragments.common.notifications.NotificationsMainFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.common.notifications.NotificationsMainViewModel ViewModel layer
 */
class NotificationsMainLogic : AbstractItemListLogic() {
    override val databasePath = "notifications"
    override val dbRef: Query get() = Firebase.firestore.collection("notifications")

    fun loadData(userRole: Int, callback: (MutableList<AbstractDataObject>) -> Unit) {
        dbRef.whereGreaterThan("date", ComputingUtils.getDateXDaysAgo(3)).whereEqualTo("targetUserId", Firebase.auth.uid).get()
            .addOnSuccessListener { snapshot1 ->
                dbRef.whereGreaterThanOrEqualTo("targetGroup", userRole).limit(40L).get()
                    .addOnSuccessListener { snapshot2 ->
                        retrieveDataList(snapshot1, snapshot2, callback)
                    }
            }
    }

    override fun retrieveDataList(snapshot: QuerySnapshot, callback: (MutableList<AbstractDataObject>) -> Unit) {}

    fun retrieveDataList(snapshot1: QuerySnapshot, snapshot2: QuerySnapshot, callback: (MutableList<AbstractDataObject>) -> Unit) {
        val dataList =
            snapshot1.map { document -> document.toObject<Notification>() }.toMutableList()
                .also { it.addAll(snapshot2.map { document -> document.toObject() }) }
        dataList.sortByDescending { it.date }
        callback(dataList.toMutableList())
    }
}
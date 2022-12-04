package pi.restaurantapp.logic.fragments;

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.SnapshotsPair

abstract class AbstractPreviewItemLogic : AbstractFragmentLogic() {
    protected val dbRefBasic get() = Firebase.firestore.collection("$databasePath-basic")
    protected val dbRefDetails get() = Firebase.firestore.collection("$databasePath-details")

    private val snapshotsPair = SnapshotsPair()

    fun getDataFromDatabase(itemId: String, callback: (SnapshotsPair) -> Unit) {
        dbRefBasic.document(itemId).get().addOnSuccessListener { snapshot ->
            snapshotsPair.basic = snapshot
            if (snapshotsPair.isReady()) {
                callback(snapshotsPair)
            }
        }
        dbRefDetails.document(itemId).get().addOnSuccessListener { snapshot ->
            snapshotsPair.details = snapshot
            if (snapshotsPair.isReady()) {
                callback(snapshotsPair)
            }
        }
    }

}

package pi.restaurantapp.logic.fragments;

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.SnapshotsPair

/**
 * Abstract class responsible for business logic and communication with database (Model layer) for AbstractPreviewItemFragment.
 * @see pi.restaurantapp.ui.fragments.AbstractPreviewItemFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel ViewModel layer
 */
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

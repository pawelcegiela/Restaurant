package pi.restaurantapp.objects

import com.google.firebase.firestore.DocumentSnapshot

/**
 * Data transport class containing two document snapshots - for basic information and details.
 */
class SnapshotsPair {
    var basic: DocumentSnapshot? = null
    var details: DocumentSnapshot? = null

    fun isReady(): Boolean {
        return basic != null && details != null
    }
}
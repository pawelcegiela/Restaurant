package pi.restaurantapp.objects

import com.google.firebase.firestore.DocumentSnapshot

class SnapshotsPair {
    var basic: DocumentSnapshot? = null
    var details: DocumentSnapshot? = null

    fun isReady(): Boolean {
        return basic != null && details != null
    }
}
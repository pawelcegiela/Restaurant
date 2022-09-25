package pi.restaurant.management.utils

import com.google.firebase.database.DataSnapshot

class SnapshotsPair {
    var basic: DataSnapshot? = null
    var details: DataSnapshot? = null

    fun isReady(): Boolean {
        return basic != null && details != null
    }
}
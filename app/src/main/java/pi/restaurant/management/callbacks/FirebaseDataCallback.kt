package pi.restaurant.management.callbacks

import com.google.firebase.database.DataSnapshot

interface FirebaseDataCallback {
    fun onCallback(dataSnapshot: DataSnapshot)
}
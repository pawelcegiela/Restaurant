package pi.restaurant.management.utils

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.callbacks.FirebaseDataCallback
import pi.restaurant.management.enums.Role
import pi.restaurant.management.fragments.AbstractModifyItemFragment

class FirebaseUtils {
    companion object {
        fun checkPrivileges(fragment: AbstractModifyItemFragment) {
            val userId = Firebase.auth.uid ?: return
            val databaseRef = Firebase.database.getReference("users").child(userId).child("role")
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val myRole = dataSnapshot.getValue<Int>() ?: return
                    fragment.myRole = myRole
                    if (myRole < Role.WORKER.ordinal) {
                        fragment.initializeUINew()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }

        fun getDataFromDatabase(databasePath: String, itemId: String, callback: FirebaseDataCallback) {
            val databaseRef = Firebase.database.getReference(databasePath).child(itemId)
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    callback.onCallback(dataSnapshot)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }
}
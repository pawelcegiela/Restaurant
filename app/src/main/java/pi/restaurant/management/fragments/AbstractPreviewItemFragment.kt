package pi.restaurant.management.fragments

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.enums.Role

abstract class AbstractPreviewItemFragment : AbstractSplashScreenFragment() {
    var myRole: Int = 3

    abstract val databasePath: String
    abstract val linearLayout: LinearLayout
    lateinit var itemId: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemId = arguments?.getString("id").toString()
        checkPrivileges()
    }

    private fun checkPrivileges() {
        val userId = Firebase.auth.uid ?: return
        val databaseRef = Firebase.database.getReference("users").child(userId).child("role")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                myRole = dataSnapshot.getValue<Int>() ?: return
                if (myRole < Role.WORKER.ordinal) {
                    getDataFromDatabase()
                }
                keepSplashScreen = false
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun getDataFromDatabase() {
        val databaseRef = Firebase.database.getReference(databasePath).child(itemId)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                fillInData(dataSnapshot)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    abstract fun fillInData(dataSnapshot: DataSnapshot)
}
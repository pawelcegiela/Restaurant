package pi.restaurant.management.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.enums.Role

abstract class AbstractPreviewItemFragment : Fragment() {
    var myRole: Int = 3

    abstract val databasePath: String
    abstract val linearLayout: LinearLayout
    abstract val editButton: Button?
    abstract val editActionId: Int
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
                    unlockUI()
                    getDataFromDatabase()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    open fun unlockUI() {
        editButton?.visibility = View.VISIBLE
        editButton?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", itemId)

            findNavController().navigate(editActionId, bundle)
        }
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
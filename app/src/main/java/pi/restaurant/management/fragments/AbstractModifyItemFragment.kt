package pi.restaurant.management.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.children
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.enums.Role

abstract class AbstractModifyItemFragment : AbstractSplashScreenFragment() {
    var myRole: Int = 3

    abstract val databasePath: String
    abstract val linearLayout: LinearLayout
    abstract val saveButton: Button
    abstract val removeButton: Button
    abstract var itemId: String
    abstract val nextActionId: Int
    abstract val saveMessageId: Int
    abstract val removeMessageId: Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    initializeUI()
                }
                keepSplashScreen = false
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun unlockUI() {
        for (view in linearLayout.children) {
            view.isEnabled = true
        }
        saveButton.text = getText(R.string.save)
        removeButton.text = getText(R.string.remove_item)
    }

    abstract fun initializeUI()

    fun loadData() {
        val databaseRef = Firebase.database.getReference(databasePath).child(itemId)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                setData(dataSnapshot)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    open fun setData(dataSnapshot: DataSnapshot) {}

    fun setValue(data: AbstractDataObject) {
        val databaseRef = Firebase.database.getReference(databasePath).child(data.id)
        databaseRef.setValue(data)

        Toast.makeText(activity, getString(saveMessageId), Toast.LENGTH_SHORT).show()
        findNavController().navigate(nextActionId)
    }

    fun setRemoveButtonListener() {
        removeButton.setOnClickListener {
            val databaseRef = Firebase.database.getReference(databasePath).child(itemId)

            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        snapshot.ref.removeValue()
                    }

                    Toast.makeText(activity, getString(removeMessageId), Toast.LENGTH_SHORT).show()
                    findNavController().navigate(nextActionId)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }
}
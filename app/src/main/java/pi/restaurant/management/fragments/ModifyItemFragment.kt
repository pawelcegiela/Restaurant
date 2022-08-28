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
import pi.restaurant.management.data.BaseDataObject
import pi.restaurant.management.enums.Role

abstract class ModifyItemFragment : SplashScreenFragment() {
    var myRole: Int = 3

    abstract val databasePath: String
    abstract val linearLayout: LinearLayout
    abstract val saveButton: Button
    abstract var itemId: String
    abstract val saveActionId: Int
    abstract val toastMessageId: Int

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

    fun setValue(data: BaseDataObject) {
        val databaseRef = Firebase.database.getReference(databasePath).child(data.id)
        databaseRef.setValue(data)

        Toast.makeText(activity, getString(toastMessageId), Toast.LENGTH_SHORT).show()

        findNavController().navigate(saveActionId)
    }
}
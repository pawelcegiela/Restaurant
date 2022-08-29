package pi.restaurant.management.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
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
import pi.restaurant.management.enums.Precondition
import pi.restaurant.management.enums.Role
import pi.restaurant.management.utils.Utils

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

    fun getDataFromDatabase() {
        val databaseRef = Firebase.database.getReference(databasePath).child(itemId)
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                fillInData(dataSnapshot)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    open fun fillInData(dataSnapshot: DataSnapshot) {}

    fun setSaveButtonListener() {
        saveButton.setOnClickListener {
            if (!Utils.checkRequiredFields(getEditTextMap(), this)) {
                return@setOnClickListener
            }

            saveToDatabase()
        }
    }

    abstract fun getEditTextMap(): Map<EditText, Int>

    open fun saveToDatabase() {
        val data = getDataObject()

        val precondition = checkSavePreconditions(data)
        if (precondition != Precondition.OK) {
            Toast.makeText(activity, getString(precondition.nameRes), Toast.LENGTH_SHORT).show()
            return
        }

        val databaseRef = Firebase.database.getReference(databasePath).child(data.id)
        databaseRef.setValue(data)

        Toast.makeText(activity, getString(saveMessageId), Toast.LENGTH_SHORT).show()
        findNavController().navigate(nextActionId)
    }

    abstract fun getDataObject(): AbstractDataObject

    open fun checkSavePreconditions(data: AbstractDataObject): Precondition {
        return Precondition.OK
    }

    open fun setRemoveButtonListener() {
        removeButton.setOnClickListener {
            val databaseRef = Firebase.database.getReference(databasePath).child(itemId)

            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    removeFromDatabase(dataSnapshot)
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }

    private fun removeFromDatabase(dataSnapshot: DataSnapshot) {
        for (snapshot in dataSnapshot.children) {
            snapshot.ref.removeValue()
        }

        val dialogBuilder = AlertDialog.Builder(this.context)
        dialogBuilder.setTitle(getString(R.string.warning))
        dialogBuilder.setMessage(getString(R.string.do_you_want_to_remove))
        dialogBuilder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            dialog.dismiss()
            Toast.makeText(activity, getString(removeMessageId), Toast.LENGTH_SHORT).show()
            findNavController().navigate(nextActionId)
        }
        dialogBuilder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }
        dialogBuilder.create().show()
    }
}
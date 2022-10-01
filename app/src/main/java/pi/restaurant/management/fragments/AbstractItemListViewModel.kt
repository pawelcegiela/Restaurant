package pi.restaurant.management.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.enums.Precondition
import pi.restaurant.management.enums.Role

abstract class AbstractItemListViewModel : ViewModel() {
    abstract val databasePath: String

    val liveUserRole = MutableLiveData(Role.getPlaceholder())
    val liveDataList = MutableLiveData<MutableList<AbstractDataObject>>()
    val liveEditPrecondition = MutableLiveData<Precondition>()

    fun loadData() {
        val databaseRef = Firebase.database.getReference(databasePath).child("basic")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                setDataList(dataSnapshot)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun getUserRole() {
        val userId = Firebase.auth.uid ?: return
        val databaseRef = Firebase.database.getReference("users").child("basic").child(userId).child("role")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                liveUserRole.value = dataSnapshot.getValue<Int>() ?: return
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    abstract fun setDataList(dataSnapshot: DataSnapshot)

    open fun checkPreconditions(item: AbstractDataObject) {
        liveEditPrecondition.value = Precondition.OK
    }

}
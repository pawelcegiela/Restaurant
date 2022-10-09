package pi.restaurant.management.model.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.objects.data.AbstractDataObject
import pi.restaurant.management.objects.enums.Precondition
import pi.restaurant.management.objects.enums.Role

abstract class AbstractItemListViewModel : ViewModel() {
    abstract val databasePath: String

    private val _userRole: MutableLiveData<Int> = MutableLiveData(Role.getPlaceholder())
    val userRole: LiveData<Int> = _userRole

    private val _dataList: MutableLiveData<MutableList<AbstractDataObject>> = MutableLiveData<MutableList<AbstractDataObject>>()
    val dataList: LiveData<MutableList<AbstractDataObject>> = _dataList

    private val _editPrecondition: MutableLiveData<Precondition?> = MutableLiveData<Precondition?>()
    val editPrecondition: LiveData<Precondition?> = _editPrecondition

    fun loadData() {
        val databaseRef = Firebase.database.getReference(databasePath).child("basic")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                retrieveDataList(dataSnapshot)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun getUserRole() {
        val userId = Firebase.auth.uid ?: return
        val databaseRef = Firebase.database.getReference("users").child("basic").child(userId).child("role")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _userRole.value = dataSnapshot.getValue<Int>() ?: return
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    abstract fun retrieveDataList(dataSnapshot: DataSnapshot)

    fun setDataList(list: MutableList<AbstractDataObject>) {
        _dataList.value = list
    }

    fun setEditPrecondition(precondition: Precondition?) {
        _editPrecondition.value = precondition
    }

    open fun checkPreconditions(item: AbstractDataObject) {
        _editPrecondition.value = Precondition.OK
    }

    open fun displayFAB() = true

}
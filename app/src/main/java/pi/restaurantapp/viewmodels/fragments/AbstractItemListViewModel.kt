package pi.restaurantapp.viewmodels.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.data.AbstractDataObject

abstract class AbstractItemListViewModel : AbstractFragmentViewModel() {
    protected open val dbRef: Query get() = Firebase.firestore.collection("$databasePath-basic")

    private val _dataList: MutableLiveData<MutableList<AbstractDataObject>> = MutableLiveData<MutableList<AbstractDataObject>>()
    val dataList: LiveData<MutableList<AbstractDataObject>> = _dataList

    fun loadData() {
        dbRef.get().addOnSuccessListener { snapshot ->
            retrieveDataList(snapshot)
        }
    }

    abstract fun retrieveDataList(snapshot: QuerySnapshot)

    fun setDataList(list: MutableList<AbstractDataObject>) {
        _dataList.value = list
    }

    open fun displayFAB() = true

}
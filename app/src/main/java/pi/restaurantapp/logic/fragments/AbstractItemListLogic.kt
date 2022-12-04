package pi.restaurantapp.logic.fragments;

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.data.AbstractDataObject

abstract class AbstractItemListLogic : AbstractFragmentLogic() {
    protected open val dbRef: Query get() = Firebase.firestore.collection("$databasePath-basic")

    fun loadData(callback: (MutableList<AbstractDataObject>) -> Unit) {
        dbRef.get().addOnSuccessListener { snapshot ->
            retrieveDataList(snapshot, callback)
        }
    }

    abstract fun retrieveDataList(snapshot: QuerySnapshot, callback: (MutableList<AbstractDataObject>) -> Unit)
}

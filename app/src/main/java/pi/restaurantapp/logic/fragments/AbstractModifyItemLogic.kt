package pi.restaurantapp.logic.fragments;

import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.SplitDataObject

abstract class AbstractModifyItemLogic : AbstractFragmentLogic() {
    protected val dbRefBasic get() = Firebase.firestore.collection("$databasePath-basic")
    protected val dbRefDetails get() = Firebase.firestore.collection("$databasePath-details")

    private val snapshotsPair = SnapshotsPair()

    fun getDataFromDatabase(itemId: String, callback: (SnapshotsPair) -> Unit) {
        dbRefBasic.document(itemId).get().addOnSuccessListener { snapshot ->
            snapshotsPair.basic = snapshot
            if (snapshotsPair.isReady()) {
                callback(snapshotsPair)
            }
        }
        dbRefDetails.document(itemId).get().addOnSuccessListener { snapshot ->
            snapshotsPair.details = snapshot
            if (snapshotsPair.isReady()) {
                callback(snapshotsPair)
            }
        }
    }

    fun disableItem(itemId: String) {
        Firebase.firestore.collection("$databasePath-basic").document(itemId).update("disabled", true)
    }

    fun saveToDatabase(data: SplitDataObject, callback: (Boolean) -> Unit) {
        Firebase.firestore.runTransaction { transaction ->
            saveDocumentToDatabase(data, transaction)
        }.addOnSuccessListener {
            callback(true)
        }.addOnFailureListener {
            callback(false)
        }
    }

    open fun saveDocumentToDatabase(data: SplitDataObject, transaction: Transaction) {
        transaction.set(dbRefBasic.document(data.id), data.basic)
        transaction.set(dbRefDetails.document(data.id), data.details)
    }

    open fun removeFromDatabase(itemId: String) {
        removeAdditionalElements()
        dbRefBasic.document(itemId).delete()
        dbRefDetails.document(itemId).delete()
    }

    open fun removeAdditionalElements() {}
}

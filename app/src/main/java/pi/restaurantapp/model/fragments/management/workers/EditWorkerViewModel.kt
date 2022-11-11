package pi.restaurantapp.model.fragments.management.workers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.user.User
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.data.user.UserDetails

class EditWorkerViewModel : AbstractModifyWorkerViewModel() {
    private val _item: MutableLiveData<User> = MutableLiveData()
    val item: LiveData<User> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<UserBasic>() ?: UserBasic()
        val details = snapshotsPair.details?.toObject<UserDetails>() ?: UserDetails()
        _item.value = User(itemId, basic, details)
    }

    override fun saveDocumentToDatabase(data: SplitDataObject, transaction: Transaction) {
        val basic = data.basic as UserBasic
        val details = data.details as UserDetails

        val basicToChange = hashMapOf<String, Any>(
            "firstName" to basic.firstName,
            "lastName" to basic.lastName,
            "role" to basic.role,
            "delivery" to basic.delivery
        )

        transaction.update(dbRefBasic.document(data.id), basicToChange)
        transaction.update(dbRefDetails.document(data.id), "contactPhone", details.contactPhone)
    }
}
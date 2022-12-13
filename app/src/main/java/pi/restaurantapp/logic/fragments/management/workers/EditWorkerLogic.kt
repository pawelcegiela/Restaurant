package pi.restaurantapp.logic.fragments.management.workers

import com.google.firebase.firestore.Transaction
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.data.user.UserDetails

/**
 * Class responsible for business logic and communication with database (Model layer) for EditWorkerFragment.
 * @see pi.restaurantapp.ui.fragments.management.workers.EditWorkerFragment View layer
 * @see pi.restaurantapp.viewmodels.fragments.management.workers.EditWorkerViewModel ViewModel layer
 */
class EditWorkerLogic : AbstractModifyWorkerLogic() {
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
package pi.restaurantapp.logic.fragments.client.settings

import com.google.firebase.firestore.Transaction
import pi.restaurantapp.logic.fragments.AbstractModifyItemLogic
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.objects.data.user.UserDetails

class ClientMyDataLogic : AbstractModifyItemLogic() {
    override val databasePath = "users"

    override fun saveDocumentToDatabase(data: SplitDataObject, transaction: Transaction) {
        val basic = data.basic as UserBasic
        val details = data.details as UserDetails

        val basicToChange = hashMapOf<String, Any>(
            "firstName" to basic.firstName,
            "lastName" to basic.lastName
        )

        val detailsToChange = hashMapOf(
            "defaultDeliveryAddress" to details.defaultDeliveryAddress,
            "contactPhone" to details.contactPhone,
            "preferredCollectionType" to details.preferredCollectionType,
            "preferredOrderPlace" to details.preferredOrderPlace
        )

        transaction.update(dbRefBasic.document(data.id), basicToChange)
        transaction.update(dbRefDetails.document(data.id), detailsToChange)
    }
}
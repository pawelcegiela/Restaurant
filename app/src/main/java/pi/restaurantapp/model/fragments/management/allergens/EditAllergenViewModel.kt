package pi.restaurantapp.model.fragments.management.allergens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.allergen.Allergen
import pi.restaurantapp.objects.data.allergen.AllergenBasic
import pi.restaurantapp.objects.data.allergen.AllergenDetails

class EditAllergenViewModel : AbstractModifyAllergenViewModel() {
    private val _item: MutableLiveData<Allergen> = MutableLiveData()
    val item: LiveData<Allergen> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<AllergenBasic>() ?: AllergenBasic()
        val details = snapshotsPair.details?.toObject<AllergenDetails>() ?: AllergenDetails()
        _item.value = Allergen(itemId, basic, details)
    }

    override fun saveDocumentToDatabase(data: SplitDataObject, transaction: Transaction) {
        transaction.update(dbRefBasic.document(data.id), "name", (data.basic as AllergenBasic).name)
        transaction.update(dbRefDetails.document(data.id), "description", (data.details as AllergenDetails).description)
    }
}
package pi.restaurant.management.model.fragments.management.allergens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.objects.SnapshotsPair
import pi.restaurant.management.objects.data.allergen.Allergen
import pi.restaurant.management.objects.data.allergen.AllergenBasic
import pi.restaurant.management.objects.data.allergen.AllergenDetails

class EditAllergenViewModel : AbstractModifyAllergenViewModel() {
    private val _item: MutableLiveData<Allergen> = MutableLiveData()
    val item: LiveData<Allergen> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.getValue<AllergenBasic>() ?: AllergenBasic()
        val details = snapshotsPair.details?.getValue<AllergenDetails>() ?: AllergenDetails()
        _item.value = Allergen(itemId, basic, details)
    }
}
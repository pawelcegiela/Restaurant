package pi.restaurantapp.viewmodels.fragments.management.allergens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.management.allergens.PreviewAllergenLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.allergen.Allergen
import pi.restaurantapp.objects.data.allergen.AllergenBasic
import pi.restaurantapp.objects.data.allergen.AllergenDetails
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for PreviewAllergenFragment.
 * @see pi.restaurantapp.logic.fragments.management.allergens.PreviewAllergenLogic Model layer
 * @see pi.restaurantapp.ui.fragments.management.allergens.PreviewAllergenFragment View layer
 */
class PreviewAllergenViewModel : AbstractPreviewItemViewModel() {
    override val logic = PreviewAllergenLogic()

    private val _containingDishes = MutableLiveData<ArrayList<String>>(ArrayList())
    val containingDishes: LiveData<ArrayList<String>> = _containingDishes

    private val _item: MutableLiveData<Allergen> = MutableLiveData()
    val item: LiveData<Allergen> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<AllergenBasic>() ?: AllergenBasic()
        val details = snapshotsPair.details?.toObject<AllergenDetails>() ?: AllergenDetails()
        _item.value = Allergen(itemId, basic, details)

        logic.getContainingDishes(details.containingDishes.map { it.key }) { containingDishes ->
            _containingDishes.value = containingDishes
            setReadyToUnlock()
        }
    }

    override fun isDisabled(): Boolean {
        return item.value?.basic?.disabled == true
    }

}
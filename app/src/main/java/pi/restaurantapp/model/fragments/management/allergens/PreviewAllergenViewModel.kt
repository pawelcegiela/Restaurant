package pi.restaurantapp.model.fragments.management.allergens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.model.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.allergen.Allergen
import pi.restaurantapp.objects.data.allergen.AllergenBasic
import pi.restaurantapp.objects.data.allergen.AllergenDetails

class PreviewAllergenViewModel : AbstractPreviewItemViewModel() {
    override val databasePath = "allergens"

    val liveContainingDishes = MutableLiveData<ArrayList<String>>()
    val containingDishes = ArrayList<String>()

    private val _item: MutableLiveData<Allergen> = MutableLiveData()
    val item: LiveData<Allergen> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<AllergenBasic>() ?: AllergenBasic()
        val details = snapshotsPair.details?.toObject<AllergenDetails>() ?: AllergenDetails()
        _item.value = Allergen(itemId, basic, details)
    }

    fun getContainingDishes(containingDishesIds: List<String>) {
        for (id in containingDishesIds) {
            Firebase.firestore.collection("dishes-basic").document(id).get().addOnSuccessListener { snapshot ->
                containingDishes.add(snapshot.getString("name") ?: "")
                if (containingDishes.size == containingDishesIds.size) {
                    liveContainingDishes.value = containingDishes
                }
            }
        }
    }

    override fun isDisabled(): Boolean {
        return item.value?.basic?.disabled == true
    }

}
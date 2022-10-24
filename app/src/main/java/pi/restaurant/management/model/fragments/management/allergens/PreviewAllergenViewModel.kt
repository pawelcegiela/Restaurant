package pi.restaurant.management.model.fragments.management.allergens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.model.fragments.management.AbstractPreviewItemViewModel
import pi.restaurant.management.objects.SnapshotsPair
import pi.restaurant.management.objects.data.allergen.Allergen
import pi.restaurant.management.objects.data.allergen.AllergenBasic
import pi.restaurant.management.objects.data.allergen.AllergenDetails

class PreviewAllergenViewModel : AbstractPreviewItemViewModel() {
    override val databasePath = "allergens"

    val liveContainingDishes = MutableLiveData<ArrayList<String>>()
    val containingDishes = ArrayList<String>()

    private val _item: MutableLiveData<Allergen> = MutableLiveData()
    val item: LiveData<Allergen> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.getValue<AllergenBasic>() ?: AllergenBasic()
        val details = snapshotsPair.details?.getValue<AllergenDetails>() ?: AllergenDetails()
        _item.value = Allergen(itemId, basic, details)
    }

    fun getContainingDishes(containingDishesIds: List<String>) {
        for (id in containingDishesIds) {
            val databaseRef = Firebase.database.getReference("dishes").child("basic").child(id).child("name")
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    containingDishes.add(dataSnapshot.getValue<String>() ?: "")
                    if (containingDishes.size == containingDishesIds.size) {
                        liveContainingDishes.value = containingDishes
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    override fun isDisabled(): Boolean {
        return item.value?.basic?.disabled == true
    }

}
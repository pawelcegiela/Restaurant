package pi.restaurant.management.model.fragments.ingredients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.model.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.objects.SnapshotsPair
import pi.restaurant.management.objects.data.ingredient.Ingredient
import pi.restaurant.management.objects.data.ingredient.IngredientBasic
import pi.restaurant.management.objects.data.ingredient.IngredientDetails

class PreviewIngredientViewModel : AbstractPreviewItemViewModel() {
    override val databasePath = "ingredients"

    val liveContainingDishes = MutableLiveData<ArrayList<String>>()
    val liveContainingSubDishes = MutableLiveData<ArrayList<String>>()

    val containingDishes = ArrayList<String>()
    val containingSubDishes = ArrayList<String>()

    private val _item: MutableLiveData<Ingredient> = MutableLiveData()
    val item: LiveData<Ingredient> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.getValue<IngredientBasic>() ?: IngredientBasic()
        val details = snapshotsPair.details?.getValue<IngredientDetails>() ?: IngredientDetails()
        _item.value = Ingredient(itemId, basic, details)
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

    fun getContainingSubDishes(containingSubDishesIds: List<String>) {
        for (id in containingSubDishesIds) {
            val databaseRef = Firebase.database.getReference("ingredients").child("basic").child(id).child("name")
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    containingSubDishes.add(dataSnapshot.getValue<String>() ?: "")
                    if (containingSubDishes.size == containingSubDishesIds.size) {
                        liveContainingSubDishes.value = containingSubDishes
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
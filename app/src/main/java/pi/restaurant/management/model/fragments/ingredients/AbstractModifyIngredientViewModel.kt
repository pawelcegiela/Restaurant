package pi.restaurant.management.model.fragments.ingredients

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.model.fragments.AbstractModifyItemViewModel
import pi.restaurant.management.objects.data.SplitDataObject
import pi.restaurant.management.objects.data.ingredient.Ingredient
import pi.restaurant.management.objects.data.ingredient.IngredientBasic
import pi.restaurant.management.objects.data.ingredient.IngredientDetails

abstract class AbstractModifyIngredientViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "ingredients"

    val liveAllIngredients = MutableLiveData<MutableList<IngredientBasic>>()

    override fun saveToDatabase(data: SplitDataObject) {
        updateSubIngredients(data.details as IngredientDetails)
        super.saveToDatabase(data)
    }

    fun getPreviousItem(): Ingredient {
        if (this is EditIngredientViewModel) {
            return item.value ?: Ingredient(itemId, IngredientBasic(), IngredientDetails())
        }
        return Ingredient(itemId, IngredientBasic(), IngredientDetails())
    }

    private fun updateSubIngredients(details: IngredientDetails) {
        val previousSubIngredients = getPreviousItem().details.subIngredients ?: ArrayList()
        val subIngredients = details.subIngredients ?: ArrayList()
        val databaseRef = Firebase.database.getReference("ingredients").child("details")

        for (subIngredient in previousSubIngredients) {
            if (subIngredient.id !in subIngredients.map { it.id }) {
                databaseRef.child(subIngredient.id).child("containingSubDishes").child(details.id).setValue(null)
            }
        }

        for (subIngredient in subIngredients) {
            databaseRef.child(subIngredient.id).child("containingSubDishes").child(details.id).setValue(true)
        }
    }

    fun getAllIngredients() {
        val databaseRef = Firebase.database.getReference("ingredients").child("basic")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue<HashMap<String, IngredientBasic>>() ?: HashMap()
                liveAllIngredients.value = data.toList().map { it.second }.filter { !it.subDish }.toMutableList()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

}
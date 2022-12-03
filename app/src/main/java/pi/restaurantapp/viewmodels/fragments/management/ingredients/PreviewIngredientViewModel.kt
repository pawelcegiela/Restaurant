package pi.restaurantapp.viewmodels.fragments.management.ingredients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.ingredient.Ingredient
import pi.restaurantapp.objects.data.ingredient.IngredientAmountChange
import pi.restaurantapp.objects.data.ingredient.IngredientBasic
import pi.restaurantapp.objects.data.ingredient.IngredientDetails
import pi.restaurantapp.objects.enums.IngredientModificationType
import java.lang.Integer.max

class PreviewIngredientViewModel : AbstractPreviewItemViewModel() {
    override val databasePath = "ingredients"

    val containingDishes = ArrayList<String>()
    val containingSubDishes = ArrayList<String>()

    private val _item: MutableLiveData<Ingredient> = MutableLiveData()
    val item: LiveData<Ingredient> = _item

    var amountChanges: MutableList<IngredientAmountChange> = ArrayList()

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<IngredientBasic>() ?: IngredientBasic()
        val details = snapshotsPair.details?.toObject<IngredientDetails>() ?: IngredientDetails()
        _item.value = Ingredient(itemId, basic, details)

        getAmountChanges(details.amountChanges)
        getContainingDishes(details.containingDishes.map { it.key }, details.containingSubDishes.map { it.key })
    }

    private fun getAmountChanges(amountChangesHashMap: HashMap<String, IngredientAmountChange>) {
        val firstIndex = max(amountChangesHashMap.size - 10, 0)
        val lastIndex = amountChangesHashMap.size
        amountChanges = amountChangesHashMap.map { it.value }.sortedByDescending { it.date }.subList(firstIndex, lastIndex).toMutableList()
    }

    private fun getContainingDishes(containingDishesIds: List<String>, containingSubDishesIds: List<String>) {
        if (containingDishesIds.isEmpty() && containingSubDishesIds.isEmpty()) {
            setReadyToUnlock()
            return
        }

        for (id in containingDishesIds) {
            Firebase.firestore.collection("dishes-basic").document(id).get().addOnSuccessListener { snapshot ->
                containingDishes.add(snapshot.getString("name") ?: "")
                if (containingDishes.size == containingDishesIds.size && containingSubDishes.size == containingSubDishesIds.size) {
                    setReadyToUnlock()
                }
            }
        }

        for (id in containingSubDishesIds) {
            Firebase.firestore.collection("ingredients-basic").document(id).get().addOnSuccessListener { snapshot ->
                containingSubDishes.add(snapshot.getString("name") ?: "")
                if (containingSubDishes.size == containingSubDishesIds.size && containingDishes.size == containingDishesIds.size) {
                    setReadyToUnlock()
                }
            }
        }
    }

    override fun isDisabled(): Boolean {
        return item.value?.basic?.disabled == true
    }

    fun updateIngredientAmount(
        id: String,
        _amount: Int,
        modificationType: IngredientModificationType,
        setNewAmount: (Int) -> (Unit),
        addNewAmountChange: (IngredientAmountChange) -> (Unit)
    ) {
        var newAmount = 0
        var newAmountChange = IngredientAmountChange()
        Firebase.firestore.runTransaction { transaction ->
            val difference = _amount * (if (modificationType == IngredientModificationType.CORRECTION) -1 else 1)
            val oldAmount = transaction.get(dbRefBasic.document(id)).getLong("amount")?.toInt() ?: 0
            newAmount = max(oldAmount + difference, 0)
            transaction.update(dbRefBasic.document(id), "amount", newAmount)

            newAmountChange = IngredientAmountChange(Firebase.auth.uid!!, difference, newAmount, modificationType.ordinal)
            transaction.update(dbRefDetails.document(id), "amountChanges.${newAmountChange.date.time}", newAmountChange)
        }.addOnSuccessListener {
            setNewAmount(newAmount)
            addNewAmountChange(newAmountChange)
        }
    }

}
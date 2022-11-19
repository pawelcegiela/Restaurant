package pi.restaurantapp.model.fragments.management.ingredients

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import pi.restaurantapp.model.fragments.AbstractModifyItemViewModel
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.ingredient.Ingredient
import pi.restaurantapp.objects.data.ingredient.IngredientBasic
import pi.restaurantapp.objects.data.ingredient.IngredientDetails
import pi.restaurantapp.objects.data.ingredient.IngredientItem


abstract class AbstractModifyIngredientViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "ingredients"

    private val _item: MutableLiveData<Ingredient> = MutableLiveData()
    val item: LiveData<Ingredient> = _item

    private val _allIngredients = MutableLiveData<MutableList<IngredientBasic>>(ArrayList())
    val allIngredients: LiveData<MutableList<IngredientBasic>> = _allIngredients

    private var previousSubIngredients: MutableList<IngredientItem> = ArrayList()

    var observer = Observer(_item)

    class Observer(private val item: MutableLiveData<Ingredient>) : BaseObservable() {
        @get:Bindable
        var subIngredients: MutableList<IngredientItem> = ArrayList()
            set(value) {
                field = value
                notifyPropertyChanged(BR.subIngredients)
                item.value?.details?.subIngredients = value
            }
    }

    override fun saveToDatabase(data: SplitDataObject) {
        val subIngredients = (data.details as IngredientDetails).subIngredients ?: ArrayList()

        Firebase.firestore.runTransaction { transaction ->
            for (subIngredient in previousSubIngredients.filter { previousSubIngredient -> previousSubIngredient.id !in subIngredients.map { it.id } }) {
                transaction.update(dbRefDetails.document(subIngredient.id), "containingSubDishes.${data.details.id}", null)
            }
            for (subIngredient in subIngredients) {
                transaction.update(dbRefDetails.document(subIngredient.id), "containingSubDishes.${data.details.id}", true)
            }

            saveDocumentToDatabase(data, transaction)
        }.addOnSuccessListener {
            setSaveStatus(true)
        }
    }

    private fun getAllIngredients() {
        dbRefBasic.get().addOnSuccessListener { snapshot ->
            _allIngredients.value!!.addAll(snapshot
                .mapNotNull { documentSnapshot -> documentSnapshot.toObject<IngredientBasic>() }
                .filter { ingredient -> !ingredient.subDish && !ingredient.disabled })
        }
    }

    fun setItem(newItem: Ingredient) {
        _item.value = newItem
        newItem.details.subIngredients?.let { previousSubIngredients.addAll(it) }
        getAllIngredients()
    }

}
package pi.restaurantapp.model.fragments.management.dishes

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
import pi.restaurantapp.objects.data.allergen.AllergenBasic
import pi.restaurantapp.objects.data.dish.Dish
import pi.restaurantapp.objects.data.dish.DishDetails
import pi.restaurantapp.objects.data.ingredient.IngredientBasic
import pi.restaurantapp.objects.data.ingredient.IngredientItem

abstract class AbstractModifyDishViewModel : AbstractModifyItemViewModel() {
    override val databasePath = "dishes"

    private val _item: MutableLiveData<Dish> = MutableLiveData()
    val item: LiveData<Dish> = _item

    private val _allIngredients = MutableLiveData<MutableList<IngredientBasic>>()
    val allIngredients: LiveData<MutableList<IngredientBasic>> = _allIngredients

    private val _allAllergens = MutableLiveData<MutableList<AllergenBasic>>()
    val allAllergens: LiveData<MutableList<AllergenBasic>> = _allAllergens

    private var previousIngredients: MutableList<IngredientItem> = ArrayList()
    private var previousAllergens: MutableList<AllergenBasic> = ArrayList()

    var observer = Observer()

    class Observer : BaseObservable() {
        @get:Bindable
        var baseIngredients: MutableList<IngredientItem> = ArrayList()
            set(value) {
                field = value
                notifyPropertyChanged(BR.baseIngredients)
            }

        @get:Bindable
        var otherIngredients: MutableList<IngredientItem> = ArrayList()
            set(value) {
                field = value
                notifyPropertyChanged(BR.otherIngredients)
            }

        @get:Bindable
        var possibleIngredients: MutableList<IngredientItem> = ArrayList()
            set(value) {
                field = value
                notifyPropertyChanged(BR.possibleIngredients)
            }

        @get:Bindable
        var allergens: MutableList<AllergenBasic> = ArrayList()
            set(value) {
                field = value
                notifyPropertyChanged(BR.allergens)
            }
    }

    override fun saveToDatabase(data: SplitDataObject) {
        item.value?.details?.baseIngredients = HashMap(observer.baseIngredients.associateBy { it.id })
        item.value?.details?.otherIngredients = HashMap(observer.otherIngredients.associateBy { it.id })
        item.value?.details?.possibleIngredients = HashMap(observer.possibleIngredients.associateBy { it.id })
        item.value?.details?.allergens = HashMap(observer.allergens.associateBy { it.id })

        val ingredients = getIngredients()
        val allergens = (data.details as DishDetails).allergens.map { it.value }

        Firebase.firestore.runTransaction { transaction ->
            val dbRefIngredients = Firebase.firestore.collection("ingredients-details")
            for (ingredient in previousIngredients.filter { previousIngredient -> previousIngredient.id !in ingredients.map { it.id } }) {
                transaction.update(dbRefIngredients.document(ingredient.id), "containingDishes.${data.details.id}", null)
            }
            for (ingredient in ingredients) {
                transaction.update(dbRefIngredients.document(ingredient.id), "containingDishes.${data.details.id}", true)
            }

            val dbRefAllergens = Firebase.firestore.collection("allergens-details")
            for (allergen in previousAllergens.filter { previousAllergen -> previousAllergen.id !in allergens.map { it.id } }) {
                transaction.update(dbRefAllergens.document(allergen.id), "containingDishes.${data.details.id}", null)
            }
            for (allergen in allergens) {
                transaction.update(dbRefAllergens.document(allergen.id), "containingDishes.${data.details.id}", true)
            }

            saveDocumentToDatabase(data, transaction)
        }.addOnSuccessListener {
            setSaveStatus(true)
        }
    }

    fun setItem(newItem: Dish) {
        _item.value = newItem

        this.previousIngredients.addAll(getIngredients())
        this.previousAllergens.addAll(newItem.details.allergens.map { it.value })
        getAllIngredients()
        getAllAllergens()
    }

    fun getIngredients(): ArrayList<IngredientItem> {
        val details = item.value?.details ?: DishDetails()
        val baseIngredients = details.baseIngredients.map { it.value }
        val otherIngredients = details.otherIngredients.map { it.value }
        val possibleIngredients = details.possibleIngredients.map { it.value }

        val ingredients = ArrayList<IngredientItem>()
        ingredients.addAll(baseIngredients)
        ingredients.addAll(otherIngredients)
        ingredients.addAll(possibleIngredients)

        return ingredients
    }

    private fun getAllIngredients() {
        Firebase.firestore.collection("ingredients-basic").get().addOnSuccessListener { snapshot ->
            _allIngredients.value = snapshot
                .mapNotNull { documentSnapshot -> documentSnapshot.toObject<IngredientBasic>() }
                .filter { ingredient -> !ingredient.disabled }
                .toMutableList()
        }
    }

    private fun getAllAllergens() {
        Firebase.firestore.collection("allergens-basic").get().addOnSuccessListener { snapshot ->
            _allAllergens.value = snapshot
                .mapNotNull { documentSnapshot -> documentSnapshot.toObject<AllergenBasic>() }
                .filter { allergen -> !allergen.disabled }
                .toMutableList()
        }
    }
}
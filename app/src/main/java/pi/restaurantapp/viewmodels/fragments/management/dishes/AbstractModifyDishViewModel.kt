package pi.restaurantapp.viewmodels.fragments.management.dishes

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.logic.fragments.management.dishes.AbstractModifyDishLogic
import pi.restaurantapp.objects.data.NullableSplitDataObject
import pi.restaurantapp.objects.data.SplitDataObject
import pi.restaurantapp.objects.data.allergen.AllergenBasic
import pi.restaurantapp.objects.data.dish.Dish
import pi.restaurantapp.objects.data.dish.DishDetails
import pi.restaurantapp.objects.data.ingredient.IngredientBasic
import pi.restaurantapp.objects.data.ingredient.IngredientItem
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel

abstract class AbstractModifyDishViewModel : AbstractModifyItemViewModel() {
    private val _logic: AbstractModifyDishLogic get() = logic as AbstractModifyDishLogic

    private val _item: MutableLiveData<Dish> = MutableLiveData()
    val item: LiveData<Dish> = _item

    override val splitDataObject get() = NullableSplitDataObject(itemId, item.value?.basic, item.value?.details)

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

    override fun saveToDatabase2(data: SplitDataObject) {
        item.value?.details?.baseIngredients = HashMap(observer.baseIngredients.associateBy { it.id })
        item.value?.details?.otherIngredients = HashMap(observer.otherIngredients.associateBy { it.id })
        item.value?.details?.possibleIngredients = HashMap(observer.possibleIngredients.associateBy { it.id })
        item.value?.details?.allergens = HashMap(observer.allergens.associateBy { it.id })

        val ingredients = getIngredients()
        val allergens = (data.details as DishDetails).allergens.map { it.value }

        _logic.saveToDatabase(data, ingredients, previousIngredients, allergens, previousAllergens) { saveStatus ->
            setSaveStatus(saveStatus)
        }
    }

    fun setItem(newItem: Dish) {
        _item.value = newItem

        this.previousIngredients.addAll(getIngredients())
        this.previousAllergens.addAll(newItem.details.allergens.map { it.value })

        _logic.getAllIngredients { allIngredients ->
            _allIngredients.value = allIngredients
        }
        _logic.getAllAllergens { allAllergens ->
            _allAllergens.value = allAllergens
        }
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


}
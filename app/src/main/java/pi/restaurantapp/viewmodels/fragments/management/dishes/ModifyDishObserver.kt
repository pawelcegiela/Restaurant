package pi.restaurantapp.viewmodels.fragments.management.dishes

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import pi.restaurantapp.objects.data.allergen.AllergenBasic
import pi.restaurantapp.objects.data.ingredient.IngredientItem

class ModifyDishObserver : BaseObservable() {
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
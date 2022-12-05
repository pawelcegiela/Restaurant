package pi.restaurantapp.viewmodels.fragments.management.ingredients

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.objects.data.ingredient.Ingredient
import pi.restaurantapp.objects.data.ingredient.IngredientItem

class ModifyIngredientObserver(private val item: MutableLiveData<Ingredient>) : BaseObservable() {
    @get:Bindable
    var subIngredients: MutableList<IngredientItem> = ArrayList()
        set(value) {
            field = value
            notifyPropertyChanged(BR.subIngredients)
            item.value?.details?.subIngredients = value
        }
}
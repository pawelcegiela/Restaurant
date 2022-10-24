package pi.restaurantapp.model.fragments.management.dishes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ktx.getValue
import pi.restaurantapp.model.fragments.management.AbstractPreviewItemViewModel
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.dish.Dish
import pi.restaurantapp.objects.data.dish.DishBasic
import pi.restaurantapp.objects.data.dish.DishDetails
import pi.restaurantapp.utils.StringFormatUtils

class PreviewDishViewModel : AbstractPreviewItemViewModel() {
    override val databasePath = "dishes"

    private val _item: MutableLiveData<Dish> = MutableLiveData()
    val item: LiveData<Dish> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.getValue<DishBasic>() ?: DishBasic()
        val details = snapshotsPair.details?.getValue<DishDetails>() ?: DishDetails()
        _item.value = Dish(itemId, basic, details)
    }

    fun formatPrice(price: String) : String {
        return StringFormatUtils.formatPrice(price)
    }

    override fun isDisabled(): Boolean {
        return item.value?.basic?.disabled == true
    }

}
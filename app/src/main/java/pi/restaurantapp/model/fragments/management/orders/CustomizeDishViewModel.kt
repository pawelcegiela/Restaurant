package pi.restaurantapp.model.fragments.management.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ktx.getValue
import pi.restaurantapp.model.fragments.management.AbstractPreviewItemViewModel
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.dish.Dish
import pi.restaurantapp.objects.data.dish.DishBasic
import pi.restaurantapp.objects.data.dish.DishDetails
import pi.restaurantapp.objects.data.dish.DishItem

class CustomizeDishViewModel : AbstractPreviewItemViewModel() {
    override val databasePath = "dishes"

    private val _item: MutableLiveData<DishItem> = MutableLiveData()
    val item: LiveData<DishItem> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.getValue<DishBasic>() ?: DishBasic()
        val details = snapshotsPair.details?.getValue<DishDetails>() ?: DishDetails()
        val dishItem = DishItem()
        dishItem.dish = Dish(itemId, basic, details)
        _item.value = dishItem
    }

    fun getPreviousItem(): DishItem {
        return item.value ?: DishItem()
    }

    fun setItem(dishItem: DishItem) {
        _item.value = dishItem
    }

    override fun shouldGetDataFromDatabase(): Boolean {
        return _item.value == null
    }

    override fun isDisabled(): Boolean {
        return false
    }
}
package pi.restaurantapp.viewmodels.fragments.management.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.management.orders.CustomizeDishLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.dish.Dish
import pi.restaurantapp.objects.data.dish.DishBasic
import pi.restaurantapp.objects.data.dish.DishDetails
import pi.restaurantapp.objects.data.dish.DishItem
import pi.restaurantapp.objects.enums.ToolbarType
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel

class CustomizeDishViewModel : AbstractPreviewItemViewModel() {
    override val logic = CustomizeDishLogic()

    private val _item: MutableLiveData<DishItem> = MutableLiveData()
    val item: LiveData<DishItem> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<DishBasic>() ?: DishBasic()
        val details = snapshotsPair.details?.toObject<DishDetails>() ?: DishDetails()
        val dishItem = DishItem()
        dishItem.dish = Dish(itemId, basic, details)
        _item.value = dishItem
    }

    override fun setToolbarType() {
        toolbarType.value = ToolbarType.ADD
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
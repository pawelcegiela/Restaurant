package pi.restaurant.management.model.fragments.dishes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.model.fragments.AbstractPreviewItemViewModel
import pi.restaurant.management.objects.SnapshotsPair
import pi.restaurant.management.objects.data.dish.Dish
import pi.restaurant.management.objects.data.dish.DishBasic
import pi.restaurant.management.objects.data.dish.DishDetails
import pi.restaurant.management.utils.StringFormatUtils

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

}
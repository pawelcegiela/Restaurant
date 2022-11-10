package pi.restaurantapp.model.fragments.management.discounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.discount.Discount
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.data.discount.DiscountDetails

class EditDiscountViewModel : AbstractModifyDiscountViewModel() {
    private val _item: MutableLiveData<Discount> = MutableLiveData()
    val item: LiveData<Discount> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<DiscountBasic>() ?: DiscountBasic()
        val details = snapshotsPair.details?.toObject<DiscountDetails>() ?: DiscountDetails()
        _item.value = Discount(itemId, basic, details)
    }
}
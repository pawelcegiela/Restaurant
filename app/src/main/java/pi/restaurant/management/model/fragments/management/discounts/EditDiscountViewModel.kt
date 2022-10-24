package pi.restaurant.management.model.fragments.management.discounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ktx.getValue
import pi.restaurant.management.objects.SnapshotsPair
import pi.restaurant.management.objects.data.discount.Discount
import pi.restaurant.management.objects.data.discount.DiscountBasic
import pi.restaurant.management.objects.data.discount.DiscountDetails

class EditDiscountViewModel : AbstractModifyDiscountViewModel() {
    private val _item: MutableLiveData<Discount> = MutableLiveData()
    val item: LiveData<Discount> = _item

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.getValue<DiscountBasic>() ?: DiscountBasic()
        val details = snapshotsPair.details?.getValue<DiscountDetails>() ?: DiscountDetails()
        _item.value = Discount(itemId, basic, details)
    }
}
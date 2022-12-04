package pi.restaurantapp.viewmodels.fragments.management.discounts

import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.management.discounts.EditDiscountLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.discount.Discount
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.data.discount.DiscountDetails

class EditDiscountViewModel : AbstractModifyDiscountViewModel() {
    override val logic = EditDiscountLogic()

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<DiscountBasic>() ?: DiscountBasic()
        val details = snapshotsPair.details?.toObject<DiscountDetails>() ?: DiscountDetails()
        setItem(Discount(itemId, basic, details))
    }
}
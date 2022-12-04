package pi.restaurantapp.viewmodels.fragments.management.discounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.toObject
import pi.restaurantapp.logic.fragments.management.discounts.PreviewDiscountLogic
import pi.restaurantapp.objects.SnapshotsPair
import pi.restaurantapp.objects.data.discount.Discount
import pi.restaurantapp.objects.data.discount.DiscountBasic
import pi.restaurantapp.objects.data.discount.DiscountDetails
import pi.restaurantapp.objects.data.user.UserBasic
import pi.restaurantapp.viewmodels.fragments.AbstractPreviewItemViewModel

class PreviewDiscountViewModel : AbstractPreviewItemViewModel() {
    override val logic = PreviewDiscountLogic()

    private val _item: MutableLiveData<Discount> = MutableLiveData()
    val item: LiveData<Discount> = _item

    private val _customers: MutableLiveData<MutableList<UserBasic>> = MutableLiveData()
    val customers: LiveData<MutableList<UserBasic>> = _customers

    override fun getItem(snapshotsPair: SnapshotsPair) {
        val basic = snapshotsPair.basic?.toObject<DiscountBasic>() ?: DiscountBasic()
        val details = DiscountDetails()
        _item.value = Discount(itemId, basic, details)
    }

    override fun isDisabled(): Boolean {
        return item.value?.basic?.disabled == true
    }

    fun getCustomers() {
        logic.getCustomers { customers ->
            _customers.value = customers
        }
    }

    fun addCustomer(customer: UserBasic, callback: (Int, Boolean) -> (Unit)) {
        logic.addCustomer(itemId, customer, callback) {
            item.value!!.basic.assignedDiscounts.add(customer.id)
        }
    }
}
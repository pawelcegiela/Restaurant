package pi.restaurantapp.viewmodels.fragments.management.discounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.objects.data.discount.Discount
import pi.restaurantapp.viewmodels.fragments.AbstractModifyItemViewModel

abstract class AbstractModifyDiscountViewModel : AbstractModifyItemViewModel() {
    private val _item: MutableLiveData<Discount> = MutableLiveData()
    val item: LiveData<Discount> = _item

    fun setItem(newItem: Discount) {
        _item.value = newItem
    }
}
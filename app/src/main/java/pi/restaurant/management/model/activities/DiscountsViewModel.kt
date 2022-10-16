package pi.restaurant.management.model.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pi.restaurant.management.objects.data.discount.DiscountBasic

class DiscountsViewModel : ViewModel() {
    private val _list = MutableLiveData<MutableList<DiscountBasic>>()
    val list: LiveData<MutableList<DiscountBasic>> = _list

    fun setList(list: MutableList<DiscountBasic>) {
        _list.value = list
    }
}
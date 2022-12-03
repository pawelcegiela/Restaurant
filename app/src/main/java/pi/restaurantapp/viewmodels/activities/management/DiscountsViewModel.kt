package pi.restaurantapp.viewmodels.activities.management

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.objects.data.discount.DiscountBasic

class DiscountsViewModel : AbstractActivityViewModel() {
    private val _showActive = MutableLiveData(true)
    private val _showDisabled = MutableLiveData(false)

    override fun getShowActive(): Boolean {
        return _showActive.value ?: true
    }

    override fun getShowDisabled(): Boolean {
        return _showDisabled.value ?: false
    }

    override fun setShowActive(active: Boolean) {
        _showActive.value = active
    }

    override fun setShowDisabled(disabled: Boolean) {
        _showDisabled.value = disabled
    }

    private val _list = MutableLiveData<MutableList<DiscountBasic>>()
    val list: LiveData<MutableList<DiscountBasic>> = _list

    fun setList(list: MutableList<DiscountBasic>) {
        _list.value = list
    }
}
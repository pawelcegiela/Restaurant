package pi.restaurantapp.model.activities.client

import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.model.activities.management.AbstractActivityViewModel

class ClientDiscountsViewModel : AbstractActivityViewModel() {
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
}
package pi.restaurantapp.viewmodels.activities.client

import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.viewmodels.activities.management.AbstractActivityViewModel

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for client Discounts activity.
 * @see pi.restaurantapp.ui.activities.client.ClientDiscountsActivity View layer
 */
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
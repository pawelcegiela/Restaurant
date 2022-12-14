package pi.restaurantapp.viewmodels.activities.management

import androidx.lifecycle.MutableLiveData
import pi.restaurantapp.viewmodels.activities.AbstractActivityViewModel

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for Dishes activity.
 * @see pi.restaurantapp.ui.activities.management.DishesActivity View layer
 */
class DishesViewModel : AbstractActivityViewModel() {
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
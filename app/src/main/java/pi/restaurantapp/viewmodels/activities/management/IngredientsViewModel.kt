package pi.restaurantapp.viewmodels.activities.management

import androidx.lifecycle.MutableLiveData

/**
 * Class responsible for presentation logic and binding between data/model and view (ViewModel layer) for Ingredients activity.
 * @see pi.restaurantapp.ui.activities.management.IngredientsActivity View layer
 */
class IngredientsViewModel : AbstractActivityViewModel() {
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
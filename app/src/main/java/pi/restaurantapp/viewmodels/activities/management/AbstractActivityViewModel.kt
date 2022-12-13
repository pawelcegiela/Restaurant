package pi.restaurantapp.viewmodels.activities.management

import androidx.lifecycle.ViewModel

/**
 * Abstract class responsible for presentation logic and binding between data/model and view (ViewModel layer) for activities.
 */
abstract class AbstractActivityViewModel : ViewModel() {
    abstract fun getShowActive(): Boolean
    abstract fun getShowDisabled(): Boolean
    abstract fun setShowActive(active: Boolean)
    abstract fun setShowDisabled(disabled: Boolean)
}
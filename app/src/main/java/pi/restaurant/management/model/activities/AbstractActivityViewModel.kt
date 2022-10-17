package pi.restaurant.management.model.activities

import androidx.lifecycle.ViewModel


abstract class AbstractActivityViewModel : ViewModel() {
    abstract fun getShowActive(): Boolean
    abstract fun getShowDisabled(): Boolean
    abstract fun setShowActive(active: Boolean)
    abstract fun setShowDisabled(disabled: Boolean)
}
package pi.restaurantapp.ui

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * Custom manager for recycler views which disables vertical scrolling.
 */
class RecyclerManager(context: Context?) : LinearLayoutManager(context) {
    override fun canScrollVertically() = false

    companion object {
        @JvmStatic
        fun createNew(context: Context?): RecyclerManager {
            return RecyclerManager(context)
        }
    }
}
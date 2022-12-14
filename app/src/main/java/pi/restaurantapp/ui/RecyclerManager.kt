package pi.restaurantapp.ui

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler


/**
 * Custom manager for recycler views which disables vertical scrolling.
 */
class RecyclerManager(context: Context?) : LinearLayoutManager(context) {
    override fun canScrollVertically() = false

    override fun onLayoutChildren(recycler: Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (_: IndexOutOfBoundsException) {
        }
    }

    companion object {
        @JvmStatic
        fun createNew(context: Context?): RecyclerManager {
            return RecyclerManager(context)
        }
    }
}
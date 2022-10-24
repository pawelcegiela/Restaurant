package pi.restaurantapp.ui

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class RecyclerManager(context: Context?) : LinearLayoutManager(context) {
    override fun canScrollVertically() = false
}
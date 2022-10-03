package pi.restaurant.management.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.R
import pi.restaurant.management.data.IngredientItem
import pi.restaurant.management.enums.IngredientStatus
import pi.restaurant.management.views.DialogIngredientProperties

class SubItemUtils {
    companion object {
        fun setRecyclerSize(recyclerView: RecyclerView, size: Int, context: Context) {
            val itemSize = 50
            val layoutParams = LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                (itemSize * context.resources.displayMetrics.density * size).toInt()
            )
            recyclerView.layoutParams = layoutParams
        }

        fun removeIngredientItem(
            list: MutableList<IngredientItem>,
            recyclerView: RecyclerView,
            item: IngredientItem,
            context: Context
        ) {
            list.remove(item)
            recyclerView.adapter?.notifyDataSetChanged() //TODO Zła praktyka
            setRecyclerSize(recyclerView, list.size, context)
        }

    }
}
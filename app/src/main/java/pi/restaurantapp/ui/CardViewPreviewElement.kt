package pi.restaurantapp.ui

import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import pi.restaurantapp.R
import pi.restaurantapp.logic.utils.UserInterfaceUtils

/**
 * Custom card view element used in the app.
 */
class CardViewPreviewElement(context: Context, attrs: AttributeSet?) : CardView(context, attrs) {
    init {
        backgroundTintList = context.resources.getColorStateList(R.color.blue_2a, null)
        radius = dpToPx(40)
        setContentPadding(dpToPx(28).toInt(), dpToPx(15).toInt(), dpToPx(28).toInt(), dpToPx(15).toInt())
    }

    private fun dpToPx(dp: Int): Float {
        return UserInterfaceUtils.dpToPx(dp, resources)
    }
}
package pi.restaurant.management.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.setMargins
import pi.restaurant.management.R
import pi.restaurant.management.utils.Utils

class CardViewPreviewElement(context: Context, attrs: AttributeSet?) : CardView(context, attrs) {
    init {
        backgroundTintList = context.resources.getColorStateList(R.color.blue_2a, null)
        radius = dpToPx(40)
        setContentPadding(dpToPx(28).toInt(), dpToPx(15).toInt(), dpToPx(28).toInt(), dpToPx(15).toInt())
    }

    private fun dpToPx(dp: Int) : Float {
        return Utils.dpToPx(dp, resources)
    }
}
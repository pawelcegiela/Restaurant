package pi.restaurantapp.ui.textviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import pi.restaurantapp.R

/**
 * Custom text view element for titles of recycler items.
 */
class TextViewItemTitle(context: Context?, attrs: AttributeSet?) : androidx.appcompat.widget.AppCompatTextView(context!!, attrs) {
    init {
        textSize = 22F
        gravity = Gravity.START
        typeface = Typeface.DEFAULT_BOLD
        setTextColor(resources.getColor(R.color.light_grey, null))
    }
}
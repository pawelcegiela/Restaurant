package pi.restaurantapp.ui.textviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import pi.restaurantapp.R

/**
 * Custom text view element for show less/show more buttons.
 */
class TextViewLessMore(context: Context?, attrs: AttributeSet?) : androidx.appcompat.widget.AppCompatTextView(context!!, attrs) {
    init {
        textSize = 15F
        gravity = Gravity.CENTER
        setTypeface(null, Typeface.ITALIC)
        setTextColor(resources.getColor(R.color.light_grey, null))
    }
}
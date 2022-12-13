package pi.restaurantapp.ui.textviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import pi.restaurantapp.R

/**
 * Custom text view element for types of items.
 */
class TextViewItemType(context: Context?, attrs: AttributeSet?) : androidx.appcompat.widget.AppCompatTextView(context!!, attrs) {
    init {
        textSize = 16F
        gravity = Gravity.CENTER
        setTypeface(null, Typeface.ITALIC)
        textAlignment = TEXT_ALIGNMENT_CENTER
        setTextColor(resources.getColor(R.color.light_grey, null))
    }
}
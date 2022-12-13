package pi.restaurantapp.ui.textviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import pi.restaurantapp.R

/**
 * Custom text view element for names of items.
 */
class TextViewItemName(context: Context?, attrs: AttributeSet?) : androidx.appcompat.widget.AppCompatTextView(context!!, attrs) {
    init {
        textSize = 26F
        gravity = Gravity.CENTER
        typeface = Typeface.DEFAULT_BOLD
        textAlignment = TEXT_ALIGNMENT_CENTER
        setTextColor(resources.getColor(R.color.light_grey, null))
    }
}
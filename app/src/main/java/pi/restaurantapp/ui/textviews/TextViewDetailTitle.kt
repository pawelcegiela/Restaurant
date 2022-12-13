package pi.restaurantapp.ui.textviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity

/**
 * Custom text view element for detail titles.
 */
class TextViewDetailTitle(context: Context?, attrs: AttributeSet?) : androidx.appcompat.widget.AppCompatTextView(context!!, attrs) {
    init {
        textSize = 15F
        gravity = Gravity.START
        setTypeface(null, Typeface.ITALIC)
    }
}
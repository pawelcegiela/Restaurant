package pi.restaurant.management.ui.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity

class TextViewDetailTitle(context: Context?, attrs: AttributeSet?) : androidx.appcompat.widget.AppCompatTextView(context!!, attrs) {
    init {
        textSize = 15F
        gravity = Gravity.START
        setTypeface(null, Typeface.ITALIC)
    }
}
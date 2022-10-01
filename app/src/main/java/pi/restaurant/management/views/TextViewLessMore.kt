package pi.restaurant.management.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import pi.restaurant.management.R

class TextViewLessMore(context: Context?, attrs: AttributeSet?) : androidx.appcompat.widget.AppCompatTextView(context!!, attrs) {
    init {
        textSize = 15F
        gravity = Gravity.CENTER
        setTypeface(null, Typeface.ITALIC)
        setTextColor(resources.getColor(R.color.white, null))
    }
}
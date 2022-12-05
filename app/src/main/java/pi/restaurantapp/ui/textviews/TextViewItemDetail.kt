package pi.restaurantapp.ui.textviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import pi.restaurantapp.R

class TextViewItemDetail(context: Context?, attrs: AttributeSet?) : androidx.appcompat.widget.AppCompatTextView(context!!, attrs) {
    init {
        textSize = 16F
        gravity = Gravity.START
        typeface = Typeface.DEFAULT
        setTextColor(resources.getColor(R.color.light_grey, null))
    }
}
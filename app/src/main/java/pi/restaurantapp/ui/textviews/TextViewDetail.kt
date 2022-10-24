package pi.restaurantapp.ui.textviews

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity

class TextViewDetail(context: Context?, attrs: AttributeSet?) : androidx.appcompat.widget.AppCompatTextView(context!!, attrs) {
    init {
        textSize = 18F
        gravity = Gravity.START
    }
}
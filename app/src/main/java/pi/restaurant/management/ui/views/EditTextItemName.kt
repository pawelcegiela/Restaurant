package pi.restaurant.management.ui.views

import android.content.Context
import android.graphics.Typeface
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import pi.restaurant.management.R

class EditTextItemName(context: Context?, attrs: AttributeSet?) : androidx.appcompat.widget.AppCompatEditText(context!!, attrs) {
    init {
        textSize = 26F
        gravity = Gravity.CENTER
        typeface = Typeface.DEFAULT_BOLD
        textAlignment = TEXT_ALIGNMENT_CENTER
        setTextColor(resources.getColor(R.color.white, null))
        importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO
        inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
    }
}
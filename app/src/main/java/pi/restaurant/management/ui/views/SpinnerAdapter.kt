package pi.restaurant.management.ui.views

import android.content.Context
import android.widget.ArrayAdapter
import pi.restaurant.management.R

class SpinnerAdapter(context: Context, array: Array<String>) : ArrayAdapter<String>(context, R.layout.spinner_item_view, R.id.itemTextView, array) {
    init {
        setDropDownViewResource(R.layout.spinner_item_view_dropdown)
    }
}
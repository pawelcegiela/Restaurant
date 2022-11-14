package pi.restaurantapp.ui.adapters

import android.content.Context
import android.widget.ArrayAdapter
import pi.restaurantapp.R

class SpinnerAdapter(context: Context, array: Array<String>) : ArrayAdapter<String>(context, R.layout.spinner_item_view, R.id.itemTextView, array) {
    init {
        setDropDownViewResource(R.layout.spinner_item_view_dropdown)
    }

    companion object {
        @JvmStatic
        fun createNew(context: Context, array: Array<String>): SpinnerAdapter {
            return SpinnerAdapter(context, array)
        }
    }
}
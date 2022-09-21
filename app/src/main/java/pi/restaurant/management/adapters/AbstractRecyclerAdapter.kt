package pi.restaurant.management.adapters

import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.data.AbstractDataObject
import java.util.*

abstract class AbstractRecyclerAdapter<RecyclerHolder : RecyclerView.ViewHolder?> :
    RecyclerView.Adapter<RecyclerHolder>() {
    abstract val allDataSet: List<AbstractDataObject>

    fun filter(query: String?) {
        var text = query ?: return
        clearDataSet()
        if (text.isEmpty()) {
            resetDataSet()
        } else {
            text = text.lowercase(Locale.getDefault())
            for (item in allDataSet) {
                addItemToDataSet(item, text)
            }
        }
        notifyDataSetChanged()
    }

    abstract fun clearDataSet()

    abstract fun resetDataSet()

    abstract fun addItemToDataSet(item: AbstractDataObject, text: String)
}


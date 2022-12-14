package pi.restaurantapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pi.restaurantapp.databinding.ItemNotificationsBinding
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.AbstractDataObject
import pi.restaurantapp.objects.data.notification.Notification
import java.util.*

/**
 * Custom adapter for recycler view with notifications.
 */
class NotificationsRecyclerAdapter(
    private val dataSet: MutableList<Notification>,
    private val fragment: Fragment
) :
    AbstractRecyclerAdapter<NotificationsRecyclerAdapter.ViewHolder>() {
    override val allDataSet: MutableList<Notification> = ArrayList(dataSet)

    class ViewHolder(
        val binding: ItemNotificationsBinding,
        val context: Context,
        val fragment: Fragment
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationsBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context, fragment)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewName.text = dataSet[position].text
        viewHolder.binding.textViewDate.text = StringFormatUtils.formatDateTime(dataSet[position].date)
    }

    override fun getItemCount() = dataSet.size

    override fun clearDataSet() {
        dataSet.clear()
    }

    override fun resetDataSet() {
        dataSet.addAll(allDataSet)
    }

    override fun addItemToDataSet(item: AbstractDataObject, text: String) {
        val notification = item as Notification
        if (notification.text.lowercase(Locale.getDefault()).contains(text)) {
            if (notification.text.lowercase(Locale.getDefault()).contains(text)) {
                dataSet.add(notification)
            }
        }
    }

}
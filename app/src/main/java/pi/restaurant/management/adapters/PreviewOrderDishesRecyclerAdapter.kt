package pi.restaurant.management.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.R
import pi.restaurant.management.data.DishItem
import pi.restaurant.management.databinding.ItemOrderDishBinding
import pi.restaurant.management.databinding.ItemSubItemBinding
import pi.restaurant.management.fragments.orders.AbstractModifyOrderFragment
import pi.restaurant.management.utils.StringFormatUtils


class PreviewOrderDishesRecyclerAdapter(
    private val dataSet: List<DishItem>,
    private val fragment: Fragment,
) :
    RecyclerView.Adapter<PreviewOrderDishesRecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: ItemOrderDishBinding,
        val context: Context,
        val fragment: Fragment,
        private val dataSet: List<DishItem>,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderDishBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context, fragment, dataSet)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewName.text = dataSet[position].dish.basic.name
        viewHolder.binding.textViewChanges.text = StringFormatUtils.formatDishChanges(dataSet[position])
        if (viewHolder.binding.textViewChanges.text.isEmpty()) {
            viewHolder.binding.textViewChanges.visibility = View.GONE
        }
    }

    override fun getItemCount() = dataSet.size

}


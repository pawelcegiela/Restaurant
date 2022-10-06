package pi.restaurant.management.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.databinding.ItemOrderDishBinding
import pi.restaurant.management.objects.enums.OrderStatus


class StatusChangesRecyclerAdapter(
    private val dataSet: List<Pair<String, Int>>,
    private val fragment: Fragment,
) :
    RecyclerView.Adapter<StatusChangesRecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: ItemOrderDishBinding,
        val context: Context,
        val fragment: Fragment,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderDishBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context, fragment)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewName.text =
            "${dataSet[position].first}  -  ${OrderStatus.getString(dataSet[position].second, fragment.requireContext())}"
        viewHolder.binding.textViewChanges.visibility = View.GONE
    }

    override fun getItemCount() = dataSet.size

}


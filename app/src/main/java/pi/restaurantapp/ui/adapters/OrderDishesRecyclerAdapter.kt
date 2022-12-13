package pi.restaurantapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pi.restaurantapp.databinding.ItemOrderDishBinding
import pi.restaurantapp.logic.utils.StringFormatUtils
import pi.restaurantapp.objects.data.dish.DishItem
import pi.restaurantapp.ui.fragments.client.neworder.ClientOrderSummaryFragment
import pi.restaurantapp.ui.fragments.client.orders.ClientPreviewOrderFragment
import pi.restaurantapp.ui.fragments.management.orders.AbstractModifyOrderFragment
import pi.restaurantapp.ui.fragments.management.orders.PreviewOrderFragment

/**
 * Custom adapter for recycler view with dishes in an order.
 */
class OrderDishesRecyclerAdapter(
    private val dataSet: List<DishItem>,
    private val fragment: Fragment,
) :
    RecyclerView.Adapter<OrderDishesRecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: ItemOrderDishBinding,
        val context: Context,
        val fragment: Fragment,
        private val dataSet: List<DishItem>,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            if (fragment is AbstractModifyOrderFragment) {
                binding.buttonRemove.setOnClickListener {
                    fragment.removeDish(dataSet[layoutPosition])
                }
                binding.root.setOnClickListener {
                    fragment.editDish(dataSet[layoutPosition])
                }
            } else if (fragment is PreviewOrderFragment || fragment is ClientPreviewOrderFragment || fragment is ClientOrderSummaryFragment) {
                binding.buttonRemove.visibility = View.GONE
            }
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderDishBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context, fragment, dataSet)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dataSet[position]
        viewHolder.binding.textViewName.text = item.dish.basic.name
        viewHolder.binding.textViewDetails.text = StringFormatUtils.formatDishItemDetails(item)
        viewHolder.binding.textViewChanges.text = StringFormatUtils.formatDishChanges(item)
        if (viewHolder.binding.textViewChanges.text.isEmpty()) {
            viewHolder.binding.textViewChanges.visibility = View.GONE
        }
    }

    override fun getItemCount() = dataSet.size

    companion object {
        @JvmStatic
        fun createNew(dataSet: List<DishItem>, fragment: Fragment): OrderDishesRecyclerAdapter {
            return OrderDishesRecyclerAdapter(dataSet, fragment)
        }
    }

}


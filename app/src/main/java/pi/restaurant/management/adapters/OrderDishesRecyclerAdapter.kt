package pi.restaurant.management.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.R
import pi.restaurant.management.data.DishItem
import pi.restaurant.management.databinding.ItemSubItemBinding
import pi.restaurant.management.fragments.orders.AbstractModifyOrderFragment


class OrderDishesRecyclerAdapter(
    private val dataSet: List<DishItem>,
    private val fragment: Fragment,
) :
    RecyclerView.Adapter<OrderDishesRecyclerAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: ItemSubItemBinding,
        val context: Context,
        val fragment: Fragment,
        private val dataSet: List<DishItem>,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            initialize()
        }

        private fun initialize() {
            if (fragment is AbstractModifyOrderFragment) {
                binding.buttonOptions.text = fragment.getString(R.string.remove)
                binding.buttonOptions.setOnClickListener {
                    fragment.removeDish(dataSet[layoutPosition])
                }
                binding.root.setOnClickListener {

                }
            } else {
                binding.buttonOptions.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSubItemBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context, fragment, dataSet)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewName.text = dataSet[position].dish.basic.name
    }

    override fun getItemCount() = dataSet.size

}


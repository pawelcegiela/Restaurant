package pi.restaurant.management.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.R
import pi.restaurant.management.activities.DishesActivity
import pi.restaurant.management.activities.OrdersActivity
import pi.restaurant.management.data.AbstractDataObject
import pi.restaurant.management.data.DishBasic
import pi.restaurant.management.databinding.ItemDishesBinding
import pi.restaurant.management.utils.StringFormatUtils
import java.util.*


class DishesRecyclerAdapter(
    private val dataSet: MutableList<DishBasic>,
    private val fragment: Fragment,
) :
    AbstractRecyclerAdapter<DishesRecyclerAdapter.ViewHolder>() {
    override val allDataSet: MutableList<DishBasic> = ArrayList(dataSet)

    class ViewHolder(
        val binding: ItemDishesBinding,
        val context: Context,
        val fragment: Fragment,
        private val dataSet: List<DishBasic>
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                openItemPreview()
            }
        }

        private fun openItemPreview() {
            val bundle = Bundle()
            bundle.putString("id", dataSet[layoutPosition].id)

            if (fragment.activity is DishesActivity) {
                fragment.findNavController().navigate(R.id.actionDishesToPreviewDish, bundle)
            } else if (fragment.activity is OrdersActivity) {
                fragment.findNavController().navigate(R.id.actionChooseDishToCustomizeDish, bundle)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDishesBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding, viewGroup.context, fragment, dataSet)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.binding.textViewName.text = dataSet[position].name
        viewHolder.binding.textViewPrice.text = StringFormatUtils.formatPrice(dataSet[position].basePrice)
    }

    override fun getItemCount() = dataSet.size

    override fun clearDataSet() {
        dataSet.clear()
    }

    override fun resetDataSet() {
        dataSet.addAll(allDataSet)
    }

    override fun addItemToDataSet(item: AbstractDataObject, text: String) {
        val dish = item as DishBasic
        if (dish.name.lowercase(Locale.getDefault()).contains(text)) {
            dataSet.add(dish)
        }
    }

}

